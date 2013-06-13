package com.github.illya13.drools.inspect;

import org.drools.WorkingMemory;
import org.drools.core.util.debug.NodeInfo;
import org.drools.core.util.debug.SessionInspector;
import org.drools.core.util.debug.StatefulKnowledgeSessionInfo;
import org.drools.definition.rule.Rule;
import org.drools.impl.StatefulKnowledgeSessionImpl;
import org.drools.reteoo.ReteooWorkingMemory;
import org.drools.reteoo.ReteooWorkingMemoryInterface;
import org.drools.runtime.StatefulKnowledgeSession;

import java.util.LinkedList;
import java.util.List;

public class InspectorImpl implements Inspector {
    private final SessionInspector sessionInspector;
    private final List<String> nodeInfos;
    private final List<String> log;

    public InspectorImpl(ReteooWorkingMemoryInterface workingMemoryInterface) {
        this.sessionInspector = new SessionInspector(workingMemoryInterface);
        this.nodeInfos = new LinkedList<String>();
        this.log = new LinkedList<String>();

    }

    public InspectorImpl(StatefulKnowledgeSession statefulKnowledgeSession) {
        this(((StatefulKnowledgeSessionImpl) statefulKnowledgeSession).session);
    }

    public InspectorImpl(WorkingMemory workingMemory) {
        this((ReteooWorkingMemory) workingMemory);
    }

    @Override
    public void refresh() {
        nodeInfos.clear();
        StatefulKnowledgeSessionInfo statefulKnowledgeSessionInfo = sessionInspector.getSessionInfo();
        for (NodeInfo nodeInfo : statefulKnowledgeSessionInfo.getNodeInfos())
            nodeInfos.add(nodeInfoAsString(nodeInfo));

        log.clear();
        log.addAll(statefulKnowledgeSessionInfo.getLog());
    }

    @Override
    public List<String> getNodeInfos() {
        return nodeInfos;
    }

    @Override
    public String getNodeInfo(int id) {
        return nodeInfoAsString(getNodeInfoById(id));
    }

    @Override
    public List<String> getVisitingLog() {
        return log;
    }

    @Override
    public List<String> getNodeRules(int id) {
        List<String> list = new LinkedList<String>();
        NodeInfo nodeInfo = getNodeInfoById(id);
        if (nodeInfo != null) {
            for (Rule rule : nodeInfo.getRules()) {
                list.add(rule.toString());
            }
        }
        return list;
    }

    private NodeInfo getNodeInfoById(int id) {
        StatefulKnowledgeSessionInfo statefulKnowledgeSessionInfo = sessionInspector.getSessionInfo();
        for (NodeInfo nodeInfo : statefulKnowledgeSessionInfo.getNodeInfos()) {
            if (nodeInfo.getId() == id)
                return nodeInfo;
        }
        return null;
    }

    private static String nodeInfoAsString(NodeInfo nodeInfo) {
        if (nodeInfo == null)
            return "";
        return String.format("%1$s, rules=%2$d, facts=%3$d, tuples=%4$d",
                nodeInfo.getNode().toString(), nodeInfo.getRules().size(),
                nodeInfo.getFactMemorySize(), nodeInfo.getTupleMemorySize());
    }
}
