package b1nd.dodam.melon.chart.client.support;

import b1nd.dodam.melon.chart.client.data.res.ChartRes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public final class MelonChartParser {

    private MelonChartParser() {}

    public static List<ChartRes> parse(String html) {
        List<ChartRes> charts = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements50 = document.select("div#tb_list").select("tr.lst50");
        Elements elements100 = document.select("div#tb_list").select("tr.lst100");

        for (int i = 0; i < 50; i++) {
            Element element50 = elements50.get(i);
            charts.add(createChartRes(element50));
        }

        for (int i = 0; i < 50; i++) {
            Element element100 = elements100.get(i);
            charts.add(createChartRes(element100));
        }

        return charts;
    }

    private static ChartRes createChartRes(Element element) {
        return new ChartRes(
                Integer.parseInt(element.select("span.rank").text()),
                element.select("div.ellipsis.rank01").text(),
                element.select("div.ellipsis.rank02").select("span").text(),
                element.select("div.ellipsis.rank03").text(),
                element.select("a.image_typeAll").select("img").attr("src")
        );
    }

}
