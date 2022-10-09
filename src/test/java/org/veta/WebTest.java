package org.veta;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.veta.data.Locale;
import org.veta.data.LocaleHW;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class WebTest {
    //Тестовые данные: [
    // ["Selenide" - "Selenide - это фреймворк для автоматизированного тестирования"],
    // ["JUnit" - "A programmer-oriented testing framework for Java" ]]

    @ValueSource(strings = {"Selenide", "JUnit"}) //ValueSource работает только со String и примитив типами даных
    @ParameterizedTest(name = "Проверка числа результатов поиска в Яндексе для запроса {0}")
    void yandexSearchCommonTest(String testData) {
        open("https://ya.ru");
        $("#text").setValue(testData);
        $("button[type='submit']").click();
        $$("li.serp-item")
                .shouldHave(CollectionCondition.size(10))
                .first()
                .shouldHave(text(testData));
    }

    @CsvSource(value = {
            "Selenide, Selenide - это фреймворк для автоматизированного тестирования",
            "JUnit, JUnit.org"
            //если запятая это часть текста, то тогла delimiter = '|' - можно использовать как разделитель
    })
    @ParameterizedTest(name = "Проверка числа результатов поиска в Яндексе для запроса {0}")
    void yandexSearchCommonTestDifferentExpectedText(String searchQuery, String expectedText) {
        open("https://ya.ru");
        $("#text").setValue(searchQuery);
        $("button[type='submit']").click();
        $$("li.serp-item")
                .shouldHave(CollectionCondition.size(10))
                .first()
                .shouldHave(text(expectedText));

    }

    static Stream<Arguments> selenideSiteButtonsTextDataProvider() {
        return Stream.of(
                Arguments.of(Locale.EN, List.of("Quick Start", "Docs", "FAQ",
                        "Blog", "Javadoc", "Users", "Quotes")),
                Arguments.of(Locale.RU, List.of("С чего начать?", "Док", "ЧАВО",
                        "Блог", "Javadoc", "Пользователи", "Отзывы"))
        ); //один набор параметров для запуска теста (т.к. 2 локали)
    }

    @MethodSource("selenideSiteButtonsTextDataProvider")
    // если имя метода теста и метода датапровайдера сопадает, то можно не писать
    @ParameterizedTest(name = "Check buttons names for locale: {0}")
    void selenideSiteButtonsText(Locale locale, List<String> buttonsTexts) {
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).click();
        $$(".main-menu-pages a").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }

    @EnumSource(Locale.class)
    @ParameterizedTest
    void checkLocaleTest(Locale locale) {
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).shouldBe(visible);
    }
}
