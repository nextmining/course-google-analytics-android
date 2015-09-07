package com.yglab.googleanalyticscourse.android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ProductContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<ProductItem> ITEMS = new ArrayList<ProductItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ProductItem> ITEM_MAP = new HashMap<String, ProductItem>();

    static {
        // Add 4 sample items.
        addItem(new ProductItem("1",
                "분석지표 설계 방법 과정",
                "9월4일 오전 10:00 ~ 12:00(2시간)",
                10000,
                "모바일앱 서비스를 위한 주요 지표에 대한 이해 및 다양한 외부 분석 소스 활용 방법에 대해 알아봅니다."));
        addItem(new ProductItem("2",
                "Google Analytics 활용방법 과정",
                "9월5일 오전 10:00 ~ 12:00(2시간)",
                15000,
                "Google Analytics에 대한 기본 개념 및 계정 세팅방법, Goal 설정방법 및 다양한 리포트 분석방법에 대해 알아봅니다."));
        addItem(new ProductItem("3",
                "Google Analytics 실습 과정",
                "9월5일 오후 1:00 ~ 2:00(1시간)",
                20000,
                "안드로이드 앱에 Google Analytics 트랙킹 소스를 삽입하여 GA를 통한 분석 방법 및 과정에 대해서 실습을 통해 익혀 봅니다."));
        addItem(new ProductItem("4",
                "Big Data 분석 기초 과정",
                "9월7일 오전 10:00 ~ 11:00(1시간)",
                10000,
                "향후 서비스가 대박이 난 경우, 대용량 로그 데이터 분석 및 빅데이터 분석을 활용해 데이터 상품화를 위한 기초적인 지식에 대해 알아봅니다."));
    }

    private static void addItem(ProductItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ProductItem implements Serializable {
        public String id;
        public String title;
        public String schedule;
        public int price;
        public String desc;

        public ProductItem(String id, String title, String schedule, int price, String desc) {
            this.id = id;
            this.title = title;
            this.schedule = schedule;
            this.price = price;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return title;
        }
    }

}
