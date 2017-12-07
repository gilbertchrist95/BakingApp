package com.jogoler.android.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Gilbert on 9/16/2017.
 */

public class BakingAppUpdateService extends IntentService{

    public static final String INGREDIENTS_LIST = "ingredients_list";

    public BakingAppUpdateService() {
        super("BakingAppUpdateService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            ArrayList<String> ingredientsList = intent.getExtras().getStringArrayList(INGREDIENTS_LIST);
            handleActionUpdateBakingWidgets(ingredientsList);
        }
    }

    private void handleActionUpdateBakingWidgets(ArrayList<String> ingredientsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(INGREDIENTS_LIST,ingredientsList);
        sendBroadcast(intent);
    }



    public static void startBakingService(Context context, ArrayList<String> ingredientsWidget) {
        Intent intent = new Intent(context, BakingAppUpdateService.class);
        intent.putExtra(INGREDIENTS_LIST, ingredientsWidget);
        context.startService(intent);
    }

}
