Правила игры

Далее стандартный текст:

Этот репозиторий будет склонирован для каждого студента и доступен по адресу http://ctddev.ifmo.ru:25231/git/<user>/java-advanced-2020-solutions, где <user> — имя пользователя и пароль, которые вам пришлют на @niuitmo.ru

Для сдачи домашних заданий

    Клонируйте ваш личный репозиторий
        git clone http://ctddev.ifmo.ru:25231/git/<user>/java-advanced-2020-solutions
        У личных репозиториев нет web-интерфейса, используйте инструменты командной строки.
    Добавьте ссылку на исходный репозиторий
        git remote add source https://www.kgeorgiy.info/git/geo/java-advanced-2020-solutions
        По мере появления новых домашних заданий в исходном репозитории будут появляться заготовки решений забирайте из через git pull source.
    Переименуйте пакет ru.ifmo.rain.__last_name__, заменив __last_name__ на вашу фамилию.
        В остальном сохраняйте текущую структуру каталогов и имена файлов.
        Если структура репозитория не соответствует исходной, преподаватель не будет проверять решение.
    Добавляйте только исходные файлы решений
    Вы можете редактировать .gitignore как вам удобно
    Отправьте решение на проверку
        Проверьте, что все исходники компилируются
        Проверьте, что тесты сдаваемого ДЗ проходят
        Закоммитьте все изменения в master
        Запушите все изменения
        Запросите проверку решения, заполнив форму
    После проверки преподаватель либо укажет найденные недостатки в NOTES.md, либо укажет их в виде комментариев в исходном коде, пометив их как :NOTE:

Тесты

Для запуска тестов можно воспользоваться следующим шаблоном команды:

Запускать из директории java-advanced-2020-solutions\out\production>:

java -cp .\java-advanced-2020-solutions -p ..\..\java-advanced-2020\lib;..\..\java-advanced-2020\artifacts -m info.kgeorgiy.java.advanced.implementor jar-class ru.ifmo.rain.khlytin.implementor.JarImplementor

Домашнее задание 10. HelloUDP

Интерфейсы

    HelloUDPClient должен реализовывать интерфейс HelloClient
    HelloUDPServer должен реализовывать интерфейс HelloServer

Тестирование

    простой вариант:
        клиент: info.kgeorgiy.java.advanced.hello client <полное имя класса>
        сервер: info.kgeorgiy.java.advanced.hello server <полное имя класса>
    сложный вариант:
        на противоположной стороне находится система, дающая ответы на различных языках
        клиент: info.kgeorgiy.java.advanced.hello client-i18n <полное имя класса>
        сервер: info.kgeorgiy.java.advanced.hello server-i18n <полное имя класса>
    продвинутый вариант:
        на противоположной стороне находится старая система, не полностью соответствующая последней спецификации
        клиент: info.kgeorgiy.java.advanced.hello client-evil <полное имя класса>
        сервер: info.kgeorgiy.java.advanced.hello server-evil <полное имя класса>

Исходный код тестов:

    Клиент
    Сервер

Домашнее задание 9. Web Crawler

Тестирование

    простой вариант: info.kgeorgiy.java.advanced.crawler easy <полное имя класса>
    сложный вариант: info.kgeorgiy.java.advanced.crawler hard <полное имя класса>

Исходный код тестов:

    интерфейсы и вспомогательные классы
    простой вариант
    сложный вариант

Домашнее задание 8. Параллельный запуск

Тестирование

    простой вариант: info.kgeorgiy.java.advanced.mapper scalar <полное имя класса>
    сложный вариант: info.kgeorgiy.java.advanced.mapper list <полное имя класса>
    продвинутый вариант: info.kgeorgiy.java.advanced.mapper advanced <полное имя класса>

Исходный код тестов:

    простой вариант
    сложный вариант
    продвинутый вариант

Домашнее задание 7. Итеративный параллелизм

Тестирование

    простой вариант: info.kgeorgiy.java.advanced.concurrent scalar <полное имя класса>

    Класс должен реализовывать интерфейс ScalarIP .

    сложный вариант: info.kgeorgiy.java.advanced.concurrent list <полное имя класса>

    Класс должен реализовывать интерфейс ListIP .

    продвинутый вариант: info.kgeorgiy.java.advanced.concurrent advanced <полное имя класса>

    Класс должен реализовывать интерфейс AdvancedIP .

Исходный код тестов:

    простой вариант
    сложный вариант
    продвинутый вариант

Домашнее задание 5. JarImplementor

Класс должен реализовывать интерфейс JarImpler .

Тестирование

    простой вариант: info.kgeorgiy.java.advanced.implementor jar-interface <полное имя класса>
    сложный вариант: info.kgeorgiy.java.advanced.implementor jar-class <полное имя класса>
    продвинутый вариант: info.kgeorgiy.java.advanced.implementor jar-advanced <полное имя класса>

Исходный код тестов:

    простой вариант
    сложный вариант
    продвинутый вариант

Домашнее задание 4. Implementor

Класс должен реализовывать интерфейс Impler .

Тестирование

    простой вариант: info.kgeorgiy.java.advanced.implementor interface <полное имя класса>
    сложный вариант: info.kgeorgiy.java.advanced.implementor class <полное имя класса>
    продвинутый вариант: info.kgeorgiy.java.advanced.implementor advanced <полное имя класса>

Исходный код тестов:

    простой вариант
    сложный вариант
    продвинутый вариант

Домашнее задание 3. Студенты

Тестирование

    простой вариант: info.kgeorgiy.java.advanced.student StudentQuery <полное имя класса>
    сложный вариант: info.kgeorgiy.java.advanced.student StudentGroupQuery <полное имя класса>

Исходный код

    простой вариант: интерфейс , тесты
    сложный вариант: интерфейс , тесты
    продвинутый вариант: интерфейс , тесты

Домашнее задание 2. ArraySortedSet

Тестирование

    простой вариант: info.kgeorgiy.java.advanced.arrayset SortedSet <полное имя класса>
    сложный вариант: info.kgeorgiy.java.advanced.arrayset NavigableSet <полное имя класса>

Исходный код тестов:

    простой вариант
    сложный вариант

Домашнее задание 1. Обход файлов

Для того, чтобы протестировать программу:

    Скачайте
        тесты
            info.kgeorgiy.java.advanced.base.jar
            info.kgeorgiy.java.advanced.walk.jar
        и библиотеки к ним:
            junit-4.11.jar
            hamcrest-core-1.3.jar
    Откомпилируйте решение домашнего задания
    Протестируйте домашнее задание
        Текущая директория должна:
            содержать все скачанные .jar файлы;
            содержать скомпилированное решение;
            не содержать скомпилированные самостоятельно тесты.
        простой вариант: java -cp . -p . -m info.kgeorgiy.java.advanced.walk Walk <полное имя класса>
        сложный вариант: java -cp . -p . -m info.kgeorgiy.java.advanced.walk RecursiveWalk <полное имя класса>

Исходный код тестов:

    простой вариант
    сложный вариант
