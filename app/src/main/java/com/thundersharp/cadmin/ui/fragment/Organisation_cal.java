package com.thundersharp.cadmin.ui.fragment;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thundersharp.cadmin.BuildConfig;
import com.thundersharp.cadmin.core.calendar.CalendarUtils;
import com.thundersharp.cadmin.core.calendar.EditActivity;
import com.thundersharp.cadmin.core.calendar.content.CalendarCursor;
import com.thundersharp.cadmin.core.calendar.content.EventCursor;
import com.thundersharp.cadmin.core.calendar.content.EventsQueryHandler;
import com.thundersharp.cadmin.core.calendar.widget.AgendaAdapter;
import com.thundersharp.cadmin.core.calendar.widget.AgendaView;
import com.thundersharp.cadmin.core.calendar.widget.EventCalendarView;
import com.thundersharp.cadmin.ui.activity.MainActivity;
import com.thundersharp.cadmin.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import static com.thundersharp.cadmin.ui.activity.MainActivity.floatingActionButton;
import static com.thundersharp.cadmin.ui.activity.MainActivity.toolbar;

public class Organisation_cal extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {

    FloatingActionButton fabchat;
    private static final String STATE_TOOLBAR_TOGGLE = "state:toolbarToggle";
    private static final int REQUEST_CODE_CALENDAR = 0;
    private static final int REQUEST_CODE_LOCATION = 1;
    private static final String SEPARATOR = ",";
    private static final int LOADER_CALENDARS = 0;
    private static final int LOADER_LOCAL_CALENDAR = 1;

    private final Organisation_cal.Coordinator mCoordinator = new Organisation_cal.Coordinator();
    private View mCoordinatorLayout;
    TextView txt;
    private CheckedTextView mToolbarToggle;
    private EventCalendarView mCalendarView;
    private AgendaView mAgendaView;
    private FloatingActionButton mFabAdd;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mDrawer,root;
    private final HashSet<String> mExcludedCalendarIds = new HashSet<>();
    Toolbar toolbarorg;
    private boolean mWeatherEnabled, mPendingWeatherEnabled;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        MainActivity.container.setBackground(null);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp,getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Add event in calendar coming soon",Toast.LENGTH_SHORT).show();
                createEvent();
            }
        });

        toolbarorg = getActivity().findViewById(R.id.toolbar);
        txt = root.findViewById(R.id.txt);

        /*((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) getActivity().findViewById(R.id.toolbar));
        //noinspection ConstantConditions
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(
                ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);*/
        setUpContentView(root);
        mCalendarView.setVisibility(View.VISIBLE);
        mCoordinator.coordinate(txt, mCalendarView, mAgendaView);
        loadEvents();
        //toggleCalendarView();
       /* mCoordinator.restoreState(savedInstanceState);
        if (savedInstanceState.getBoolean(STATE_TOOLBAR_TOGGLE, false)) {
            View toggleButton = getActivity().findViewById(R.id.toolbar_toggle_frame);
            if (toggleButton != null) { // can be null as disabled in landscape
                toggleButton.performClick();
            }
        }
*/

        return root;
    }

  /*  @Override
    public void onPrepareOptionsMenu(Menu menu) {

        switch (CalendarUtils.sWeekStart) {
            case Calendar.SATURDAY:
                menu.findItem(R.id.action_week_start_saturday).setChecked(true);
                break;
            case Calendar.SUNDAY:
                menu.findItem(R.id.action_week_start_sunday).setChecked(true);
                break;
            case Calendar.MONDAY:
                menu.findItem(R.id.action_week_start_monday).setChecked(true);
                break;
        }
        super.onPrepareOptionsMenu(menu);
    }
*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_today) {
            mCoordinator.reset();
            return true;
        }
        if (item.getItemId() == R.id.action_week_start_saturday ||
                item.getItemId() == R.id.action_week_start_sunday ||
                item.getItemId() == R.id.action_week_start_monday) {

            if (!item.isChecked()) {
                changeWeekStart(item.getItemId());
            }
            return true;
        }
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }*/


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mCoordinator.saveState(outState);
        //outState.putBoolean(STATE_TOOLBAR_TOGGLE, mToolbarToggle.isChecked());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mCalendarView.deactivate();
        mAgendaView.setAdapter(null); // force detaching adapter
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit()
                .putString(CalendarUtils.PREF_CALENDAR_EXCLUSIONS,
                        TextUtils.join(SEPARATOR, mExcludedCalendarIds))
                .apply();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_CODE_CALENDAR:
                toggleEmptyView(false);
                loadEvents();
                break;

        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;
        if (id == LOADER_LOCAL_CALENDAR) {
            selection = CalendarContract.Calendars.ACCOUNT_TYPE + "=?";
            selectionArgs = new String[]{String.valueOf(CalendarContract.ACCOUNT_TYPE_LOCAL)};
        }
        return new CursorLoader(getContext(),
                CalendarContract.Calendars.CONTENT_URI,
                CalendarCursor.PROJECTION, selection, selectionArgs,
                CalendarContract.Calendars.DEFAULT_SORT_ORDER);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_CALENDARS:
                break;
            case LOADER_LOCAL_CALENDAR:
                if (data == null || data.getCount() == 0) {
                    createLocalCalendar();
                }
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mCalendarSelectionView.swapCursor(null, null);
    }


    private void setUpPreferences() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String exclusions = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(CalendarUtils.PREF_CALENDAR_EXCLUSIONS, null);
        if (!TextUtils.isEmpty(exclusions)) {
            mExcludedCalendarIds.addAll(Arrays.asList(exclusions.split(SEPARATOR)));
        }
        CalendarUtils.sWeekStart = sp.getInt(CalendarUtils.PREF_WEEK_START, Calendar.SUNDAY);
    }



    private void setUpContentView(View view) {
        mCoordinatorLayout = view.findViewById(R.id.coordinator_layout);
        //mCalendarSelectionView = (CalendarSelectionView) view.findViewById(R.id.list_view_calendar);
        //noinspection ConstantConditions

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawer = view.findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mToolbarToggle = (CheckedTextView) view.findViewById(R.id.toolbar_toggle);
        View toggleButton = view.findViewById(R.id.toolbar_toggle_frame);
        if (toggleButton != null) {
            // can be null as disabled in landscape
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mToolbarToggle.toggle();
                    toggleCalendarView();
                }
            });
        }
        mCalendarView = (EventCalendarView) view.findViewById(R.id.calendar_view);
        mAgendaView = (AgendaView) view.findViewById(R.id.agenda_view);

        mFabAdd = (FloatingActionButton) view.findViewById(R.id.fab);
        mFabAdd.setBackgroundColor(getResources().getColor(R.color.white));
        mFabAdd.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_chat_bubble_outline_24));
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createEvent();
                MainActivity.navController.navigate(R.id.nav_chats);

            }
        });
        //noinspection ConstantConditions
        mFabAdd.hide();
    }


    private void toggleCalendarView() {
        if (mToolbarToggle.isChecked()) {
            mCalendarView.setVisibility(View.VISIBLE);
        } else {
            mCalendarView.setVisibility(View.GONE);
        }
    }


    @SuppressWarnings("ConstantConditions")
    private void toggleEmptyView(boolean visible) {
        root.findViewById(R.id.empty).setVisibility(View.GONE);
    }

    private void changeWeekStart(@IdRes int selection) {
        switch (selection) {
            case R.id.action_week_start_saturday:
                CalendarUtils.sWeekStart = Calendar.SATURDAY;
                break;
            case R.id.action_week_start_sunday:
                CalendarUtils.sWeekStart = Calendar.SUNDAY;
                break;
            case R.id.action_week_start_monday:
                CalendarUtils.sWeekStart = Calendar.MONDAY;
                break;
        }
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit()
                .putInt(CalendarUtils.PREF_WEEK_START, CalendarUtils.sWeekStart)
                .apply();
        getActivity().supportInvalidateOptionsMenu();
        mCoordinator.reset();
    }


    private void createEvent() {
        startActivity(new Intent(getContext(), EditActivity.class));
    }


    private void loadEvents() {
        getActivity().getSupportLoaderManager().initLoader(LOADER_CALENDARS, null, this);
        getActivity().getSupportLoaderManager().initLoader(LOADER_LOCAL_CALENDAR, null, this);
        mFabAdd.show();
        mCalendarView.setCalendarAdapter(new Organisation_cal.CalendarCursorAdapter(getContext(), mExcludedCalendarIds));
        mAgendaView.setAdapter(new Organisation_cal.AgendaCursorAdapter(getContext(), mExcludedCalendarIds));

    }






    private void createLocalCalendar() {
        String name = getString(R.string.default_calendar_name);
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, BuildConfig.APPLICATION_ID);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, name);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, name);
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, 0);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, BuildConfig.APPLICATION_ID);
        new Organisation_cal.CalendarQueryHandler(getActivity().getContentResolver())
                .startInsert(0, null, CalendarContract.Calendars.CONTENT_URI
                                .buildUpon()
                                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "1")
                                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME,
                                        BuildConfig.APPLICATION_ID)
                                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                                .build()
                        , cv);
    }


    /**
     * Coordinator utility that synchronizes widgets as selected date changes
     */
    static class Coordinator {
        private static final String STATE_SELECTED_DATE = "state:selectedDate";

        private final EventCalendarView.OnChangeListener mCalendarListener
                = new EventCalendarView.OnChangeListener() {
            @Override
            public void onSelectedDayChange(long calendarDate) {
                sync(calendarDate, mCalendarView);
            }
        };

        private final AgendaView.OnDateChangeListener mAgendaListener
                = new AgendaView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(long dayMillis) {
                sync(dayMillis, mAgendaView);
            }
        };
        private TextView mTextView;
        private EventCalendarView mCalendarView;
        private AgendaView mAgendaView;
        private long mSelectedDayMillis = CalendarUtils.NO_TIME_MILLIS;

        /**
         * Set up widgets to be synchronized
         * @param textView      title
         * @param calendarView  calendar view
         * @param agendaView    agenda view
         */
        public void coordinate(@NonNull TextView textView,
                               @NonNull EventCalendarView calendarView,
                               @NonNull AgendaView agendaView) {
            if (mCalendarView != null) {
                mCalendarView.setOnChangeListener(null);
            }
            if (mAgendaView != null) {
                mAgendaView.setOnDateChangeListener(null);
            }
            mTextView = textView;
            mCalendarView = calendarView;
            mAgendaView = agendaView;
            if (mSelectedDayMillis < 0) {
                mSelectedDayMillis = CalendarUtils.today();
            }
            mCalendarView.setSelectedDay(mSelectedDayMillis);
            agendaView.setSelectedDay(mSelectedDayMillis);
            //TODO here too
            updateTitle(mSelectedDayMillis);
            calendarView.setOnChangeListener(mCalendarListener);
            agendaView.setOnDateChangeListener(mAgendaListener);
        }

        /*void saveState(Bundle outState) {
            outState.putLong(STATE_SELECTED_DATE, mSelectedDayMillis);
        }*/

        void restoreState(Bundle savedState) {
            mSelectedDayMillis = savedState.getLong(STATE_SELECTED_DATE,
                    CalendarUtils.NO_TIME_MILLIS);
        }

        void reset() {
            mSelectedDayMillis = CalendarUtils.today();
            if (mCalendarView != null) {
                mCalendarView.reset();
            }
            if (mAgendaView != null) {
                mAgendaView.reset();
            }
            updateTitle(mSelectedDayMillis);
        }

        private void sync(long dayMillis, View originator) {
            mSelectedDayMillis = dayMillis;
            if (originator != mCalendarView) {
                mCalendarView.setSelectedDay(dayMillis);
            }
            if (originator != mAgendaView) {
                mAgendaView.setSelectedDay(dayMillis);
            }
            updateTitle(dayMillis);
        }

        private void updateTitle(long dayMillis) {
            toolbar.setTitle(CalendarUtils.toMonthString(mTextView.getContext(), dayMillis));
            //TODO addded here
        }
    }


    static class AgendaCursorAdapter extends AgendaAdapter {

        @VisibleForTesting
        final Organisation_cal.DayEventsQueryHandler mHandler;

        public AgendaCursorAdapter(Context context, Collection<String> excludedCalendarIds) {
            super(context);
            mHandler = new Organisation_cal.DayEventsQueryHandler(context.getContentResolver(), this,
                    excludedCalendarIds);
        }

        @Override
        protected void loadEvents(long timeMillis) {
            mHandler.startQuery(timeMillis, timeMillis, timeMillis + DateUtils.DAY_IN_MILLIS);
        }
    }


    static class CalendarCursorAdapter extends EventCalendarView.CalendarAdapter {
        private final Organisation_cal.MonthEventsQueryHandler mHandler;

        public CalendarCursorAdapter(Context context, Collection<String> excludedCalendarIds) {
            mHandler = new Organisation_cal.MonthEventsQueryHandler(context.getContentResolver(), this,
                    excludedCalendarIds);
        }

        @Override
        protected void loadEvents(long monthMillis) {
            long startTimeMillis = CalendarUtils.monthFirstDay(monthMillis),
                    endTimeMillis = startTimeMillis + DateUtils.DAY_IN_MILLIS *
                            CalendarUtils.monthSize(monthMillis);
            mHandler.startQuery(monthMillis, startTimeMillis, endTimeMillis);
        }
    }


    static class DayEventsQueryHandler extends EventsQueryHandler {

        private final Organisation_cal.AgendaCursorAdapter mAgendaCursorAdapter;

        public DayEventsQueryHandler(ContentResolver cr,
                                     Organisation_cal.AgendaCursorAdapter agendaCursorAdapter,
                                     @NonNull Collection<String> excludedCalendarIds) {
            super(cr, excludedCalendarIds);
            mAgendaCursorAdapter = agendaCursorAdapter;
        }

        @Override
        protected void handleQueryComplete(int token, Object cookie, EventCursor cursor) {
            mAgendaCursorAdapter.bindEvents((Long) cookie, cursor);
        }
    }

    static class MonthEventsQueryHandler extends EventsQueryHandler {

        private final Organisation_cal.CalendarCursorAdapter mAdapter;

        public MonthEventsQueryHandler(ContentResolver cr,
                                       Organisation_cal.CalendarCursorAdapter adapter,
                                       @NonNull Collection<String> excludedCalendarIds) {
            super(cr, excludedCalendarIds);
            mAdapter = adapter;
        }

        @Override
        protected void handleQueryComplete(int token, Object cookie, EventCursor cursor) {
            mAdapter.bindEvents((Long) cookie, cursor);
        }
    }

    static class CalendarQueryHandler extends AsyncQueryHandler {

        public CalendarQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }
}