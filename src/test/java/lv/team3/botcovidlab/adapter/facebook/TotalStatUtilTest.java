package lv.team3.botcovidlab.adapter.facebook;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.processors.CovidStatsProcessor;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;

import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(CovidStatsProcessor.class)
@PowerMockIgnore({"org.mockito.*"})
class TotalStatUtilTest {

    @BeforeTestClass
    public void setUp() {
        PowerMockito.mockStatic(CovidStatsProcessor.class);
    }

    @Rule
    List<CovidStats> emptyList = new ArrayList<>();


    @Test
    void countTotalThirtyDays() {

    }

    @Test
    void countTotalSevenDays() {
    }

    @Test
    void countTotalYesterday() {

    }
}