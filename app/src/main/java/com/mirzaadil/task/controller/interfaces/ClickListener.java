package com.mirzaadil.task.controller.interfaces;

import android.view.View;

/**
 * Created by mirzaadil on 8/24/17.
 */

public interface ClickListener {
    void onClick(View view,int position);
    void onLongClick(View view , int position);
}
