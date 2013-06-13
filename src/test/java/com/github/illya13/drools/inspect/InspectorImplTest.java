package com.github.illya13.drools.inspect;

import junit.framework.Assert;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class InspectorImplTest {
    private Inspector inspector;
    private StatefulKnowledgeSession ksession;

    @Before
    public void before() throws Exception {
        ksession = createStatefulKnowledgeSession();
        inspector = new InspectorImpl(ksession);
    }

    @Test
    public void getReteNodesTest() {
        inspector.refresh();
        Assert.assertNotNull(inspector.getNodeInfos());
        Assert.assertNotNull(inspector.getNodeInfo(1));
        Assert.assertNotNull(inspector.getNodeRules(1));
        Assert.assertNotNull(inspector.getVisitingLog());

        ksession.fireAllRules();
        inspector.refresh();
        Assert.assertNotNull(inspector.getNodeInfos());
        Assert.assertNotNull(inspector.getNodeInfo(1));
        Assert.assertNotNull(inspector.getNodeRules(1));
        Assert.assertNotNull(inspector.getVisitingLog());
    }


    private StatefulKnowledgeSession createStatefulKnowledgeSession() throws Exception {
        // load up the knowledge base
        KnowledgeBase kbase = readKnowledgeBase();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");

        ksession.addEventListener( new DebugAgendaEventListener() );
        ksession.addEventListener( new DebugWorkingMemoryEventListener() );

        // Rules: GO !

        Calendar calendar = GregorianCalendar.getInstance();

        // today
        TradeDate today = new TradeDate();
        today.setDate(new Date());

        // yesterday
        TradeDate yesterday = new TradeDate();
        calendar.setTime(today.getDate());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        yesterday.setDate(calendar.getTime());

        // tomorrow
        TradeDate tomorrow = new TradeDate();
        calendar.setTime(today.getDate());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        tomorrow.setDate(calendar.getTime());

        Trade trade1 = new Trade();
        trade1.setId("1");
        trade1.setValueDate(tomorrow);
        trade1.setSource(Source.TICS);

        Trade trade2 = new Trade();
        trade2.setId("2");
        trade2.setValueDate(tomorrow);
        trade2.setSource(Source.MOR);

        ksession.insert(trade1);
        ksession.insert(trade2);
        ksession.insert(today);

        return ksession;
    }

    private KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("com/github/illya13/drools/inspect/Trades.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }
}
