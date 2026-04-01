# ✈️ Тестовое задание — Gridnine Systems

Решение тестового задания для компании [Gridnine Systems](https://gridnine.com/). Задача: реализовать набор правил фильтрации перелётов на Java.

## Задача

Дан набор тестовых перелётов (`FlightBuilder`). Необходимо реализовать модуль фильтрации, который исключает перелёты по заданным правилам:

1. **Вылет до текущего момента** — рейсы, у которых дата вылета в прошлом
2. **Прилёт раньше вылета** — сегменты с некорректными датами
3. **Время на земле > 2 часов** — суммарное время между сегментами превышает 2 часа

## Решение

Применён паттерн **Strategy**: каждое правило фильтрации — отдельный класс, реализующий общий интерфейс `FlightFilter`. Это позволяет легко добавлять новые правила и комбинировать существующие.

```
solution/src/com/gridnine/testing/
├── FlightFilter.java                  # Интерфейс фильтра
├── DeparturePastFilter.java           # Правило 1: вылет в прошлом
├── ArrivalBeforeDepartureFilter.java  # Правило 2: прилёт раньше вылета
├── GroundTimeExceededFilter.java      # Правило 3: время на земле > 2ч
├── Flight.java                        # Модель перелёта
├── Segment.java                       # Модель сегмента
├── FlightBuilder.java                 # Фабрика тестовых данных
├── Main.java                          # Точка входа, демонстрация работы
└── test/
    ├── DeparturePastFilterTest.java           # JUnit-тесты
    ├── ArrivalBeforeDepartureFilterTest.java
    └── GroundTimeExceededFilterTest.java
```

## Стек

- Java (чистая, без фреймворков)
- JUnit (тесты)
- Stream API
- Паттерн Strategy

## Запуск

```bash
# Компиляция
javac -d out solution/src/com/gridnine/testing/*.java

# Запуск
java -cp out com.gridnine.testing.Main
```

## Тесты

```bash
# Компиляция с JUnit в classpath
javac -cp "out:junit-platform-console-standalone.jar" \
  -d out solution/src/com/gridnine/testing/test/*.java

# Запуск тестов
java -jar junit-platform-console-standalone.jar --class-path out \
  --scan-class-path
```

## Анкета

Ответы на теоретические вопросы — в файле [`questionary.md`](questionary.md).
