package com.jogoler.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.jogoler.android.bakingapp.R;

import java.util.List;

import static com.jogoler.android.bakingapp.widget.BakingAppWidget.ingredientsList;

/**
 * Created by Gilbert on 9/16/2017.
 */

public class GridWidgetService extends RemoteViewsService {
    List<String> ingredientList;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(),intent);
    }

    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        Context mContext;

        public GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            ingredientList = ingredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ingredientList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget_item);
            remoteViews.setTextViewText(R.id.widget_item_text_view,ingredientList.get(i));
            Intent fillInIntent = new Intent();
            remoteViews.setOnClickFillInIntent(R.id.widget_item_text_view,fillInIntent);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
