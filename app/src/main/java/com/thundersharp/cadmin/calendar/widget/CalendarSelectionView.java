package com.thundersharp.cadmin.calendar.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.provider.CalendarContract;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.calendar.ViewUtils;
import com.thundersharp.cadmin.calendar.content.CalendarCursor;

import java.util.Set;

public class CalendarSelectionView extends ListView {
    private final SimpleCursorAdapter mCursorAdapter;
    private OnSelectionChangeListener mListener;
    private final int[] mColors;

    public interface OnSelectionChangeListener {
        void onSelectionChange(long id, boolean enabled);
    }

    public CalendarSelectionView(Context context) {
        this(context, null);
    }

    public CalendarSelectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarSelectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            mColors = new int[]{ContextCompat.getColor(context, android.R.color.transparent)};
        } else {
            mColors = ViewUtils.getCalendarColors(context);
        }
        mCursorAdapter = new CalendarCursorAdapter(context);
        TypedArray ta = context.getTheme().obtainStyledAttributes(new int[]{
                R.attr.selectableItemBackground
        });
        setSelector(ta.getDrawable(0));
        ta.recycle();
        setDrawSelectorOnTop(true);
        setChoiceMode(CHOICE_MODE_MULTIPLE);
        setAdapter(mCursorAdapter);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onSelectionChange(id, isItemChecked(position));
                }
            }
        });
    }

    public void setOnSelectionChangeListener(OnSelectionChangeListener listener) {
        mListener = listener;
    }

    public void swapCursor(@Nullable CalendarCursor cursor, @Nullable Set<String> exclusions) {
        mCursorAdapter.swapCursor(cursor);
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                if (exclusions != null &&
                        !exclusions.contains(String.valueOf(mCursorAdapter.getItemId(i)))) {
                    setItemChecked(i, true);
                }
            }
        }
    }

    class CalendarCursorAdapter extends SimpleCursorAdapter {
        private static final long NO_ID = -1;

        public CalendarCursorAdapter(Context context) {
            super(context,
                    R.layout.list_item_calendar,
                    null,
                    new String[]{CalendarContract.Calendars.CALENDAR_DISPLAY_NAME},
                    new int[]{R.id.text_view_title},
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
            view.setBackgroundColor(mColors[
                    Math.abs((int) (((CalendarCursor) cursor).getId() % mColors.length))]);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @SuppressLint("RestrictedApi")
        @Override
        public long getItemId(int position) {
            if (mCursor == null || !mCursor.moveToPosition(position)) {
                return NO_ID;
            }
            return ((CalendarCursor) mCursor).getId();
        }
    }
}
