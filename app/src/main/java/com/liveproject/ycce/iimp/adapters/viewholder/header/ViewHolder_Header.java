package com.liveproject.ycce.iimp.adapters.viewholder.header;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.liveproject.ycce.iimp.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by Tiger on 28-02-2017.
 */

public class ViewHolder_Header extends GroupViewHolder {

    private TextView Name;
    private ImageView arrow;

    public ViewHolder_Header(View itemView) {
        super(itemView);
        Name = (TextView) itemView.findViewById(R.id.list_item_header_name);
        arrow = (ImageView) itemView.findViewById(R.id.list_item_header_arrow);
    }

    public void setParentTitle(ExpandableGroup parent) {
            Name.setText(parent.getTitle());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}
