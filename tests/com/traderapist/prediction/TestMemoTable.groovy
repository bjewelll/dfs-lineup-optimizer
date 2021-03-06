package com.traderapist.prediction

import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 8/31/13
 * Time: 10:51 PM
 * To change this template use File | Settings | File Templates.
 */
class TestMemoTable{
	MemoTable table

	@Before
	void setUp() {
		table = new MemoTable()
	}

	@After
	void tearDown() {
		table = null
	}

	@Test
	void testWriteSolution_FirstItem() {
		table.initializeItemsList("FLEX")
		MemoItem item = new MemoItem(cost: 3500, points: 100, roster: ["Dan"])

		table.writeSolution(0, item)

		assert table.items[0].size() == 1
	}

	@Test
	void testWriteSolution_OverlappingItems_SamePoints() {
		table.initializeItemsList("FLEX")
		MemoItem item = new MemoItem(cost: 3500, points: 100, roster: ["Dan"])
		table.writeSolution(0, item)

		assert table.items[0].size() == 1

		MemoItem dupe = new MemoItem(cost: 3500, points: 100, roster: ["Dan"])
		table.writeSolution(0, dupe)

		assert table.items[0].size() == 1
        assert table.items[0][0].points == 100
	}

	@Test
	void testWriteSolution_OverlappingItems_LowerPoints() {
		table.initializeItemsList("FLEX")
		MemoItem item = new MemoItem(cost: 3500, points: 100, roster: ["Dan"])
		table.writeSolution(0, item)

        assert table.items[0].size() == 1

		MemoItem dupe = new MemoItem(cost: 3500, points: 90, roster: ["Dan"])
		table.writeSolution(0, dupe)

        assert table.items[0].size() == 1
        assert table.items[0][0].points == 100
	}

    @Test
    void testWriteSolution_OverlappingItems_HigherPoints() {
	    table.initializeItemsList("FLEX")
        MemoItem item = new MemoItem(cost: 3500, points: 100, roster: ["Dan"])
        table.writeSolution(0, item)

        assert table.items[0].size() == 1

        MemoItem dupe = new MemoItem(cost: 3500, points: 200, roster: ["Better"])
        table.writeSolution(0, dupe)

        assert table.items[0].size() == 1
        assert table.items[0][0].points == 200 && table.items[0][0].roster == ["Better"]
    }

	@Test
	void testWriteSolution_NonOverlappingItems() {
		table.initializeItemsList("QB")

		/*
		 * Write entries into the table.
		 */
		MemoItem item = new MemoItem(cost: 3500, points: 100, roster: ["Dan"])
		table.writeSolution(0, item)

		assert table.items[0].size() == 1

		item = new MemoItem(cost: 4000, points: 110, roster: ["Tom"])
		table.writeSolution(0, item)

		assert table.items[0].size() == 2

		/*
		 * Read entries from the table.
		 */
		item = table.getSolution(0, 3500)
		assert item.points == 100 && item.roster == ["Dan"]

		item = table.getSolution(0, 4000)
		assert item.points == 110 && item.roster == ["Tom"]

		assert table.getSolution(0, 3800) == null
	}

	@Test
	void testWriteSolution_NonOverlappingItems_2() {
		table.initializeItemsList("QB,RB")

		/*
		 * Write entries into the table.
		 */
		MemoItem item = new MemoItem(cost: 3500, points: 100, roster: ["Dan"])
		table.writeSolution(0, item)

		assert table.items[0].size() == 1

		item = new MemoItem(cost: 4000, points: 110, roster: ["Tom"])
		table.writeSolution(0, item)

		assert table.items[0].size() == 2

		item = new MemoItem(cost: 3800, points: 120, roster: ["RB,Dan"])
		table.writeSolution(1, item)
		assert table.items[1].size() == 1

		item = new MemoItem(cost: 3900, points: 125, roster: ["RB2,Tom"])
		table.writeSolution(1, item)
		assert table.items[1].size() == 2
	}

	@Test
	void testInitializeItemList_QB() {
		table.initializeItemsList("QB")
		assert table.items.size() == 1
	}

	@Test
	void testInitializeItemList_QB_RB() {
		table.initializeItemsList("QB,RB")
		assert table.items.size() == 2
	}

	@Test
	void testInitializeItemList_QB_RB_WR() {
		table.initializeItemsList("QB,RB,WR")
		assert table.items.size() == 3
	}

	@Test
	void testInitializeItemList_QB_WR_WR_FLEX() {
		table.initializeItemsList("QB,WR,WR,FLEX")
		assert table.items.size() == 4
	}
}
