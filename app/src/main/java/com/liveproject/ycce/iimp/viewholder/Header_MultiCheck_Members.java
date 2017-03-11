package com.liveproject.ycce.iimp.viewholder;

import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;

import java.util.List;

/**
 * Created by Tiger on 06-03-2017.
 */

public class Header_MultiCheck_Members extends MultiCheckExpandableGroup {

    public Header_MultiCheck_Members(String title, List<MemberPersonalInfo> items) {
        super(title, items);
    }
}
