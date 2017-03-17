package com.liveproject.ycce.iimp.adapters.headers;

import com.liveproject.ycce.iimp.GroupCondition;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Tiger on 16-03-2017.
 */

public class Header_GroupCondition extends ExpandableGroup<GroupCondition> {

    public Header_GroupCondition(String title, List<GroupCondition> items) {
        super(title, items);
    }
}