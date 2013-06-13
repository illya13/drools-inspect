package com.github.illya13.drools.inspect;

import java.util.List;

public interface Inspector {
    void refresh();
    List<String> getNodeInfos();
    String getNodeInfo(int id);
    List<String> getVisitingLog();
    List<String> getNodeRules(int id);
}
