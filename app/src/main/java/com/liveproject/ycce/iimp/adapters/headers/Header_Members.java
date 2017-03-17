package com.liveproject.ycce.iimp.adapters.headers;

import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Tiger on 28-02-2017.
 */

public class Header_Members extends ExpandableGroup<MemberPersonalInfo> {

    public Header_Members(String title, List<MemberPersonalInfo> items) {
        super(title, items);
    }
}
