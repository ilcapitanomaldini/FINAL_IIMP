package com.liveproject.ycce.iimp.adapters.headers;

import com.liveproject.ycce.iimp.Roles;
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;

import java.util.List;

/**
 * Created by Tiger on 02-03-2017.
 */

public class Header_MultiCheck_Roles extends MultiCheckExpandableGroup {

    public Header_MultiCheck_Roles(String title, List<Roles> items) {
        super(title, items);
    }
}
