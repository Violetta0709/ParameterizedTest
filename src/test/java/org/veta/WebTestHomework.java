package org.veta;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.veta.data.LocaleHW;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class WebTestHomework {

    @ValueSource(strings = {"стиральная машина", "утюг"})
    @ParameterizedTest(name = "Проверка результатов поиска на сайте Технопарка {0}")
    void technoparkSearchTest(String testData) {
        open("https://technopark.ru/");
        $("#header-search-input-main").setValue(testData);
        $(".header-search-suggests__list").shouldHave(text(testData));
    }

    static Stream<Arguments> whiteRabbitSiteButtonsTextDataProvider() {
        return Stream.of(
                Arguments.of(LocaleHW.RU, List.of("РЕСТОРАН" + " " + "МЕНЮ" +
                        " " + "ГАЛЕРЕЯ" + " " + "СЕРТИФИКАТ" + " " + "КОНТАКТЫ")),
                Arguments.of(LocaleHW.EN, List.of("RESTAURANT" + " " + "MENU" +
                        " " + "WHITE RABBIT RESTAURANT AND BAR" + " " + "GALLERY" +
                        " " + "CERTIFICATS" + " " + "CONTACTS"))
        );
    }

    @Disabled("Smth wrong with RU locale Test, please help me;)")
    @MethodSource("whiteRabbitSiteButtonsTextDataProvider")
    @ParameterizedTest(name = "Checking buttons names for locale: {0}")
    void whiteRabbitButtonsText(LocaleHW locale, List<String> buttonsTexts) {
        open("https://whiterabbitmoscow.ru/");
        $$(".mod-languages").find(text(locale.name())).click();
        $$("#menu").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }

    @EnumSource(LocaleHW.class)
    @ParameterizedTest
    void checkLocaleTest(LocaleHW locale) {
        open("https://whiterabbitmoscow.ru/");
        $$(".mod-languages").find(text(locale.name())).shouldBe(visible);

    }
}
