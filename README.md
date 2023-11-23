## Домашнее задание 1. Обход файлов

    Разработайте класс Walk, осуществляющий подсчет хеш-сумм файлов.
        Формат запуска:
    
    java Walk <входной файл> <выходной файл>
    
    Входной файл содержит список файлов, которые требуется обойти.
    Выходной файл должен содержать по одной строке для каждого файла. Формат строки:
    
    <шестнадцатеричная хеш-сумма> <путь к файлу>
    
    Для подсчета хеш-суммы используйте алгоритм SHA-256 (поддержка есть в стандартной библиотеке).
    Если при чтении файла возникают ошибки, укажите в качестве его хеш-суммы все нули.
    Кодировка входного и выходного файлов — UTF-8.
    Размеры файлов могут превышать размер оперативной памяти.
    Пример
    
Входной файл
 |          |
|----------|    
| samples/1 |
| samples/12 |
| samples/123 |
| samples/1234 |
| samples/1 |
| samples/binary |
| samples/no-such-file |
                            
    
    Выходной файл
    

|                |                    |  
| ------------- |:------------------:|
| 6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b | samples/1 | 
| 6b51d431df5d7f141cbececcf79edf3dd861c3b4069f0b11661a3eefacbba918 | samples/12 |
| a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3 | samples/123 |
| 03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4 | samples/1234 |
| 6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b | samples/1 |
| 40aff2e9d2d8922e47afd4648e6967497158785fbd1da870e7110266bf944880 | samples/binary |
| 0000000000000000000000000000000000000000000000000000000000000000 | samples/no-such-file |
                            

При выполнении задания следует обратить внимание на:
    Дизайн и обработку исключений, диагностику ошибок.
    Программа должна корректно завершаться даже в случае ошибки.
    Корректная работа с вводом-выводом.
    Отсутствие утечки ресурсов.
    Возможность повторного использования кода. 
Требования к оформлению задания.
    Проверяется исходный код задания.
    Весь код должен находиться в пакете info.kgeorgiy.ja.фамилия.walk. 

[Репозиторий курса](https://www.kgeorgiy.info/git/geo/java-advanced-2023)

## Домашнее задание 2. Множество на массиве

    Разработайте класс ArraySet, реализующий неизменяемое упорядоченное множество.
        Класс ArraySet должен реализовывать интерфейс SortedSet (простой вариант) или NavigableSet (сложный вариант).
        Все операции над множествами должны производиться с максимально возможной асимптотической эффективностью. 
    При выполнении задания следует обратить внимание на:
        Применение стандартных коллекций.
        Избавление от повторяющегося кода. 

## Домашнее задание 3. Студенты

    Разработайте класс StudentDB, осуществляющий поиск по базе данных студентов.
        Класс StudentDB должен реализовывать интерфейс StudentQuery (простой вариант) или GroupQuery (сложный вариант).
        Каждый метод должен состоять из ровно одного оператора. При этом длинные операторы надо разбивать на несколько строк. 
    При выполнении задания следует обратить внимание на:
        применение лямбда-выражений и потоков;
        избавление от повторяющегося кода. 

## Домашнее задание 4. Implementor

    Реализуйте класс Implementor, генерирующий реализации классов и интерфейсов.
        Аргумент командной строки: полное имя класса/интерфейса, для которого требуется сгенерировать реализацию.
        В результате работы должен быть сгенерирован java-код класса с суффиксом Impl, расширяющий (реализующий) указанный класс (интерфейс).
        Сгенерированный класс должен компилироваться без ошибок.
        Сгенерированный класс не должен быть абстрактным.
        Методы сгенерированного класса должны игнорировать свои аргументы и возвращать значения по умолчанию. 
    В задании выделяются три варианта:
        Простой — Implementor должен уметь реализовывать только интерфейсы (но не классы). Поддержка generics не требуется.
        Сложный — Implementor должен уметь реализовывать и классы, и интерфейсы. Поддержка generics не требуется.
        Бонусный — Implementor должен уметь реализовывать generic-классы и интерфейсы. Сгенерированный код должен иметь корректные параметры типов и не порождать UncheckedWarning. 

## Домашнее задание 5. Jar Implementor

Это домашнее задание связано с предыдущим и будет приниматься только с ним. Предыдущее домашнее задание отдельно сдать будет нельзя.

    Создайте .jar-файл, содержащий скомпилированный Implementor и сопутствующие классы.
        Созданный .jar-файл должен запускаться командой java -jar.
        Запускаемый .jar-файл должен принимать те же аргументы командной строки, что и класс Implementor. 
    Модифицируйте Implemetor так, чтобы при запуске с аргументами -jar имя-класса файл.jar он генерировал .jar-файл с реализацией соответствующего класса (интерфейса).
    Для проверки, кроме исходного кода так же должны быть представлены:
        скрипт для создания запускаемого .jar-файла, в том числе исходный код манифеста;
        запускаемый .jar-файл. 
    Сложный вариант. Решение должно быть модуляризовано. 

## Домашнее задание 6. Javadoc

Это домашнее задание связано с двумя предыдущими и будет приниматься только с ними. Предыдущие домашнее задание отдельно сдать будет нельзя.

    Документируйте класс Implementor и сопутствующие классы с применением Javadoc.
        Должны быть документированы все классы и все члены классов, в том числе private.
        Документация должна генерироваться без предупреждений.
        Сгенерированная документация должна содержать корректные ссылки на классы стандартной библиотеки. 
    Для проверки, кроме исходного кода так же должны быть представлены:
        скрипт для генерации документации;
        сгенерированная документация. 

## Домашнее задание 7. Итеративный параллелизм

    Реализуйте класс IterativeParallelism, который будет обрабатывать списки в несколько потоков.
    В простом варианте должны быть реализованы следующие методы:
        minimum(threads, list, comparator) — первый минимум;
        maximum(threads, list, comparator) — первый максимум;
        all(threads, list, predicate) — проверка, что все элементы списка, удовлетворяют предикату;
        any(threads, list, predicate) — проверка, что существует элемент списка, удовлетворяющий предикату.
        count(threads, list, predicate) — подсчёт числа элементов списка, удовлетворяющих предикату. 
    В сложном варианте должны быть дополнительно реализованы следующие методы:
        filter(threads, list, predicate) — вернуть список, содержащий элементы удовлетворяющие предикату;
        map(threads, list, function) — вернуть список, содержащий результаты применения функции;
        join(threads, list) — конкатенация строковых представлений элементов списка. 
    Во все функции передается параметр threads — сколько потоков надо использовать при вычислении. Вы можете рассчитывать, что число потоков относительно мало.
    Не следует рассчитывать на то, что переданные компараторы, предикаты и функции работают быстро.
    При выполнении задания нельзя использовать Concurrency Utilities. 

## Домашнее задание 8. Параллельный запуск

    Напишите класс ParallelMapperImpl, реализующий интерфейс ParallelMapper.

```java
public interface ParallelMapper extends AutoCloseable {
    <T, R> List<R> map(
        Function<? super T, ? extends R> f,
        List<? extends T> args
    ) throws InterruptedException;
    
    @Override
    void close();
    }
```

        Метод map должен параллельно вычислять функцию f на каждом из указанных аргументов (args).
        Метод close должен останавливать все рабочие потоки.
        Конструктор ParallelMapperImpl(int threads) создает threads рабочих потоков, которые могут быть использованы для распараллеливания.
        К одному ParallelMapperImpl могут одновременно обращаться несколько клиентов.
        Задания на исполнение должны накапливаться в очереди и обрабатываться в порядке поступления.
        В реализации не должно быть активных ожиданий. 
    Доработайте класс IterativeParallelism так, чтобы он мог использовать ParallelMapper.
        Добавьте конструктор IterativeParallelism(ParallelMapper)
        Методы класса должны делить работу на threads фрагментов и исполнять их при помощи ParallelMapper.
        При наличии ParallelMapper сам IterativeParallelism новые потоки создавать не должен.
        Должна быть возможность одновременного запуска и работы нескольких клиентов, использующих один ParallelMapper. 
    При выполнении задания всё ещё нельзя использовать Concurrency Utilities. 

## Домашнее задание 9. Web Crawler

    Напишите потокобезопасный класс WebCrawler, который будет рекурсивно обходить сайты.
        Класс WebCrawler должен иметь конструктор

```java
public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost)
```

            downloader позволяет скачивать страницы и извлекать из них ссылки;
            downloaders — максимальное число одновременно загружаемых страниц;
            extractors — максимальное число страниц, из которых одновременно извлекаются ссылки;
            perHost — максимальное число страниц, одновременно загружаемых c одного хоста. Для определения хоста следует использовать метод getHost класса URLUtils из тестов. 
        Класс WebCrawler должен реализовывать интерфейс Crawler

```java
public interface Crawler extends AutoCloseable {
    Result download(String url, int depth);

    void close();
}
```
            Метод download должен рекурсивно обходить страницы, начиная с указанного URL, на указанную глубину и возвращать список загруженных страниц и файлов. Например, если глубина равна 1, то должна быть загружена только указанная страница. Если глубина равна 2, то указанная страница и те страницы и файлы, на которые она ссылается, и так далее.
            Метод download может вызываться параллельно в нескольких потоках.
            Загрузка и обработка страниц (извлечение ссылок) должна выполняться максимально параллельно, с учетом ограничений на число одновременно загружаемых страниц (в том числе с одного хоста) и страниц, с которых загружаются ссылки.
            Для распараллеливания разрешается создать до downloaders + extractors вспомогательных потоков.
            Повторно загружать и/или извлекать ссылки из одной и той же страницы в рамках одного обхода (download) запрещается.
            Метод close должен завершать все вспомогательные потоки. 
        Для загрузки страниц должен применяться Downloader, передаваемый первым аргументом конструктора.

```java
public interface Downloader {
    public Document download(final String url) throws IOException;
}```

            Метод download загружает документ по его адресу (URL).
            Документ позволяет получить ссылки по загруженной странице:

```java
public interface Document {
    List<String> extractLinks() throws IOException;
}```

            Ссылки, возвращаемые документом, являются абсолютными и имеют схему http или https. 
        Должен быть реализован метод main, позволяющий запустить обход из командной строки
            Командная строка

```java
WebCrawler url [depth [downloads [extractors [perHost]]]]
```

            Для загрузки страниц требуется использовать реализацию CachingDownloader из тестов. 
    Версии задания
        Простая — не требуется учитывать ограничения на число одновременных закачек с одного хоста (perHost >= downloaders).
        Полная — требуется учитывать все ограничения.
        Бонусная — сделать параллельный обход в ширину. 
    Задание подразумевает активное использование Concurrency Utilities, в частности, в решении не должно быть «велосипедов», аналогичных/легко сводящихся к классам из Concurrency Utilities. 

## Домашнее задание 10. HelloUDP

    Реализуйте клиент и сервер, взаимодействующие по UDP.
    Класс HelloUDPClient должен отправлять запросы на сервер, принимать результаты и выводить их на консоль.
        Аргументы командной строки:
            имя или ip-адрес компьютера, на котором запущен сервер;
            номер порта, на который отсылать запросы;
            префикс запросов (строка);
            число параллельных потоков запросов;
            число запросов в каждом потоке.
        Запросы должны одновременно отсылаться в указанном числе потоков. Каждый поток должен ожидать обработки своего запроса и выводить сам запрос и результат его обработки на консоль. Если запрос не был обработан, требуется послать его заново.
        Запросы должны формироваться по схеме <префикс запросов><номер потока>_<номер запроса в потоке>. 
    Класс HelloUDPServer должен принимать задания, отсылаемые классом HelloUDPClient и отвечать на них.
        Аргументы командной строки:
            номер порта, по которому будут приниматься запросы;
            число рабочих потоков, которые будут обрабатывать запросы.
        Ответом на запрос должно быть Hello, <текст запроса>.
        Несмотря на то, что текущий способ получения ответа по запросу очень прост, сервер должен быть рассчитан на ситуацию, когда этот процесс может требовать много ресурсов и времени.
        Если сервер не успевает обрабатывать запросы, прием запросов может быть временно приостановлен. 

## Домашнее задание 11. Физические лица

    Добавьте к банковскому приложению возможность работы с физическими лицами.
        У физического лица (Person) можно запросить имя, фамилию и номер паспорта.
        Удалённые физические лица (RemotePerson) должны передаваться при помощи удалённых объектов.
        Локальные физические лица (LocalPerson) должны передаваться при помощи механизма сериализации, и при последующем использовании не требовать связи с сервером.
        Должна быть возможность поиска физического лица по номеру паспорта, с выбором типа возвращаемого лица.
        Должна быть возможность создания записи о физическом лице по его данным.
        У физического лица может быть несколько счетов, к которым должен предоставляться доступ (через Person).
        Счёту физического лица с идентификатором subId должен соответствовать банковский счёт с id вида passport:subId.
        Изменения, производимые со счётом в банке (создание и изменение баланса), должны быть видны всем соответствующим RemotePerson, и только тем LocalPerson, которые были созданы после этого изменения.
        Изменения в счетах, производимые через RemotePerson, должны сразу применяться глобально, а производимые через LocalPerson – только локально для этого конкретного LocalPerson. 
    Реализуйте приложение, демонстрирующее работу с физическим лицами.
        Аргументы командной строки: имя, фамилия, номер паспорта физического лица, номер счёта, изменение суммы счёта.
        Если информация об указанном физическом лице отсутствует, то оно должно быть добавлено. В противном случае – должны быть проверены его данные.
        Если у физического лица отсутствует счёт с указанным номером, то он создается с нулевым балансом.
        После обновления суммы счёта новый баланс должен выводиться на консоль. 
    Напишите тесты, проверяющие вышеуказанное поведение как банка, так и приложения.
        Для реализации тестов рекомендуется использовать JUnit (Tutorial). Множество примеров использования можно найти в тестах.
        Если вы знакомы с другим тестовым фреймворком (например, TestNG), то можете использовать его.
        Добавьте jar-файлы используемых библиотек в каталог lib вашего репозитория.
        Нельзя использовать самописные фреймворки и тесты, запускаемые через main. 
    Сложный вариант
        На каждом счету всегда должно быть неотрицательное количество денег.
        Тесты не должны рассчитывать на наличие запущенного RMI Registry.
        Создайте класс BankTests, запускающий тесты.
        Создайте скрипт, запускающий BankTests и возвращающий код (статус) 0 в случае успеха и 1 в случае неудачи.
        Создайте скрипт, запускающий тесты с использованием стандартного подхода для вашего тестового фреймворка. Код возврата должен быть как в предыдущем пункте. 
    Приложения и тесты должны находится в пакете info.kgeorgiy.ja.*.bank и его подпакетах. 

## Домашнее задание 12. HelloNonblockingUDP

    Реализуйте клиент и сервер, взаимодействующие по UDP, используя только неблокирующий ввод-вывод.
    Класс HelloUDPNonblockingClient должен иметь функциональность аналогичную HelloUDPClient, но без создания новых потоков.
    Класс HelloUDPNonblockingServer должен иметь функциональность аналогичную HelloUDPServer, но все операции с сокетом должны производиться в одном потоке.
    В реализации не должно быть активных ожиданий, в том числе через Selector.
    Обратите внимание на выделение общего кода старой и новой реализации.
    Бонусный вариант. Клиент и сервер могут перед началом работы выделить O(число потоков) памяти. Выделять дополнительную память во время работы запрещено. 
