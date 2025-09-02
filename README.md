# 1. Java8'den Java21'e kadar olan değişiklikler

| Sürüm                   | Önemli Yenilik                             | Açıklama / Mülakat Notu                                                     |
| ----------------------- | ------------------------------------------ | --------------------------------------------------------------------------- |
| **Java 8 (2014)**       | **Lambda Expressions**                     | Fonksiyonel programlamaya giriş. `(x) -> x * 2` gibi kısa fonksiyon tanımı. |
|                         | **Stream API**                             | Koleksiyonlar üzerinde fonksiyonel işlemler (`map`, `filter`, `reduce`).    |
|                         | **Optional**                               | Null güvenliği için değer sarmalayıcı.                                      |
|                         | **Default & Static Methods in Interfaces** | Interface içinde gövde tanımlanabilmesi.                                    |
|                         | **Date/Time API (java.time)**              | Eski `Date`/`Calendar` yerine modern tarih API’si.                          |
| **Java 9 (2017)**       | **Private Methods in Interfaces**          | Interface içindeki tekrar eden kodları saklamak için.                       |
|                         | **Factory Methods for Collections**        | `List.of(...)`, `Set.of(...)` ile immutable koleksiyon oluşturma.           |
| **Java 10 (2018)**      | **var (Local Variable Type Inference)**    | Lokal değişkenlerde tip çıkarımı: `var name = "Ali";`                       |
| **Java 11 (2018)**      | **Yeni String Metotları**                  | `isBlank()`, `lines()`, `repeat()`.                                         |
|                         | **HttpClient API**                         | Modern HTTP istekleri (`java.net.http.HttpClient`).                         |
| **Java 14 (2020)**      | **Switch Expressions (Final)**             | Artık kalıcı olarak geldi.                                                  |
| **Java 16 (2021)**      | **Records (Final)**                        | Immutable, veri odaklı sınıf tanımı.                                        |
|                         | **Pattern Matching for instanceof**        | `if (obj instanceof String s) { ... }` şeklinde doğrudan cast.              |
| **Java 17 (2021, LTS)** | **Sealed Classes**                         | Hangi sınıfların miras alabileceğini sınırlar.                              |
|                         | **Switch Pattern Matching (Preview)**      | Switch içinde pattern kullanımı.                                            |
| **Java 21 (2023, LTS)** | **Virtual Threads (Final)**                | Hafif thread yapısı. Yüksek concurrency’de devrimsel. Artık production-ready.|
|                         | **String Templates (Preview)**             | `STR."Hello \{name}"` ile kolay string birleştirme.                         |
|                         | **Sequenced Collections**                  | Liste/Set gibi koleksiyonlarda sıralı erişim API’si.                        |


# 2. Java JIT (Just-In-Time) Compiler

## 📌 Nedir?
**JIT Compiler (Just-In-Time Compiler)**, Java Virtual Machine (JVM) içerisinde bulunan bir bileşendir.  
Bytecode’u (derlenmiş `.class` dosyaları) **çalışma anında** (runtime) **makine koduna** çevirir.  
Bu sayede kod, doğrudan CPU tarafından çalıştırılabilir hale gelir.

Java normalde **"Write Once, Run Anywhere"** mantığıyla bytecode üretir.  
Bytecode platform bağımsızdır ancak CPU doğrudan bytecode’u anlayamaz.  
Bu noktada **JIT Compiler** devreye girerek bytecode'u optimize eder ve CPU'nun çalıştırabileceği **native machine code**’a dönüştürür.

---

## ⚙️ Ne Yapar?
- **Runtime’da derleme yapar** → Kod çalışırken çeviri yapılır.
- **Performans optimizasyonu sağlar**:
    - **Hotspot optimizasyonu**: Sık kullanılan kod parçaları (hot code) tespit edilir ve optimize edilerek tekrar derlenir.
    - **Inlining**: Sık çağrılan küçük metotlar, çağrıldığı yere gömülür.
    - **Loop unrolling**: Döngü optimizasyonu yapar.
- **Bytecode → Native Code** çevirisi sayesinde kod CPU’da daha hızlı çalışır.

---

## 🔍 JIT Çalışma Süreci
1. Java kodu `.java` dosyası olarak yazılır.
2. **javac** ile `.class` dosyasına (bytecode) derlenir.
3. JVM bytecode’u yükler.
4. **JIT Compiler**, sık kullanılan kodları algılar.
5. Bu kodları **native machine code**’a çevirir.
6. CPU artık doğrudan çalıştırabilir, böylece hız artar.

---

## 📊 Avantajları
- **Yüksek performans** (Özellikle uzun süre çalışan uygulamalarda).
- **Optimizasyon** (Hotspot, inlining, döngü iyileştirmeleri).
- **Platform bağımsızlığı korunur** (Bytecode hâlâ taşınabilir).

---

## 🚫 Dezavantajları
- İlk çalıştırmada biraz **başlangıç gecikmesi** olabilir (Çünkü runtime’da derleme yapar).
- Kısa ömürlü programlarda faydası sınırlıdır.

---

## 📝 Örnek
```java
public class Main {
    public static void main(String[] args) {
        long start = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++) {
            Math.sqrt(i);
        }
        long end = System.nanoTime();
        System.out.println("Süre: " + (end - start));
    }
}
```
> Bu kod birkaç kez çalıştırıldığında JIT devreye girer ve ikinci/üçüncü çalıştırmada hız artışı gözlenir.

## 🎯 Özet

- JIT, JVM’in içinde çalışan ve runtime’da bytecode’u native koda çeviren bir derleyicidir.

- Sık kullanılan kodu tespit eder, optimize eder ve CPU’ya hazır hale getirir.

- Performansı artırır, özellikle uzun süre çalışan uygulamalarda büyük fark yaratır.


# 3. Double-Checked Locking (DCL)

## 📌 Nedir?
**Double-Checked Locking**, çoklu thread ortamında **lazy initialization** (tembel yükleme) yaparken **gereksiz senkronizasyon maliyetini azaltmak** için kullanılan bir tasarım desenidir.

En çok **Singleton** pattern’inde görülür. Amaç:
- İlk erişimde nesneyi **sadece bir kez** oluşturmak.
- Oluşturulduktan sonra **synchronized** blok maliyetinden kaçınmak.

---

## ⚙️ Nasıl Çalışır?
1. Nesne **ilk kez** istenirse:
    - İlk kontrol (`null` kontrolü) → **Senkr. bloğa girmeden** yapılır.
2. Nesne **hala null** ise:
    - **Synchronized** blok içinde **tekrar kontrol** edilir.
    - Hâlâ null ise nesne oluşturulur.
3. Sonraki erişimlerde:
    - Senkronizasyon olmadan doğrudan nesne döndürülür.

---

## 📜 Örnek Kod
```java
public class Singleton {
    // volatile: JVM'in instruction reordering sorununu önler
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) { // 1️⃣ İlk kontrol (lock yok)
            synchronized (Singleton.class) {
                if (instance == null) { // 2️⃣ İkinci kontrol (lock içinde)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

# 4. Volatile keyword nedir?
- Java’da volatile bir değişkenin thread-safe bir şekilde görünürlüğünü sağlayan bir keyword’dür. Yani, bir thread’in yaptığı değişikliklerin diğer thread’ler tarafından anında görülmesini garanti eder.


# 5. Java Interface Metotlarında Body Olup Olmaması

## 1. Java 7 ve Öncesi
- Interface metotları **abstract**tır, body içermezler.
- Java 8 ile interface’lerde **default method** ve **static method** tanımlanabilir hale geldi.
- Java 9 ile **private method** ve **private static** method interface içinde tanımlanabilir ve body içerebilir.

# 6. Main Thread Dışında İş Yapma (Java)

- Runnable, ExecutorService, @Async gibi sınıflar veya anotasyonlar kullanılabilir.

# 7. Checked vs Unchecked Exceptions (Java)

## Checked Exceptions
- **Compile time**'da kontrol edilir.
- `try-catch` ile yakalamak veya `throws` ile bildirmek zorunludur.
- `Exception` sınıfından türeyip **RuntimeException**'dan türemeyenler.
- **Örnekler:**
    - `IOException`
    - `SQLException`
    - `FileNotFoundException`

**Örnek Kod:**
```java
public void readFile(String path) throws IOException {
    FileReader reader = new FileReader(path); // IOException
}
```

## Unchecked Exceptions
- **Runtime**'da kontrol edilir, derleyici zorunlu kılmaz.
- `try-catch` zorunlu değildir.
- `RuntimeException` sınıfından türeyenler.
- **Örnekler:**
    - `NullPointerException`
    - `ArithmeticException`
    - `ArrayIndexOutOfBoundsException`

**Örnek Kod:**
```java
public int divide(int a, int b) {
    return a / b; // b=0 => ArithmeticException
}
```

# 8. Büyük Dosya Okuma (Java & Spring Boot)

## 1. Problem
- `Files.readAllBytes()` veya `Files.readAllLines()` tüm dosyayı RAM'e yükler.
- Büyük dosyalarda **OutOfMemoryError** oluşabilir.
- Çözüm: **streaming** yaklaşımı ile dosyayı parça parça okumak.

---

## 2. Çözüm
-    BufferedReader ile Satır Satır Okuma
        - IO işlemlerini buffer ile yapmak çok daha hızlıdır.
-    Java 8 Stream API ile Satır Satır Okumak
        - Lazy-loading yapar, belleğe yüklemez.
        - `Files.lines(Path)` ile satır satır stream halinde okuma yapılır.
-   NIO (New IO) Kullanmak



# 9. Observability Stack: Grafana, Spring Cloud Sleuth, Zipkin, Prometheus

## 1️⃣ Grafana

**Açıklama:**  
Açık kaynaklı, web tabanlı **görselleştirme ve dashboard** aracıdır.  
Farklı veri kaynaklarından (Prometheus, Elasticsearch, InfluxDB vb.) gelen verileri grafik, tablo ve heatmap gibi görsellerle sunar.

**Ne işe yarar?**
- Canlı metrikleri izlemek
- Özel uyarılar (alerts) tanımlamak
- Log, metrik ve trace verilerini tek ekranda birleştirmek

**Fayda:**
> “Veri var ama tablo halinde bakmak istemiyorum” dediğin yerde, Grafana onu görsel hale getirir ve dashboard’lar sayesinde anlık durumu takip edebilirsin.

---

## 2️⃣ Spring Cloud Sleuth

**Açıklama:**  
Spring Boot uygulamaları için **distributed tracing** (dağıtık izleme) kütüphanesidir.  
Her isteğe **trace id** ve **span id** ekler.  
Farklı mikroservislerdeki logların aynı isteğe ait olduklarını anlamayı sağlar.

**Ne işe yarar?**
- Bir istek birden fazla servisten geçiyorsa hangi servislerde ne kadar zaman harcandığını görebilirsin.
- Log’larda aynı isteğe ait satırları filtreleyebilirsin.

**Fayda:**
> Mikroservislerde “Bu istek nerede takıldı?” sorusunun cevabını bulmanı kolaylaştırır.

---

## 3️⃣ Zipkin

**Açıklama:**  
Distributed tracing için veri toplayan ve görselleştiren araçtır.  
Sleuth gibi kütüphanelerden gelen **trace verilerini** toplar, saklar ve web arayüzünde gösterir.

**Ne işe yarar?**
- Bir isteğin hangi servislerden geçtiğini **timeline** şeklinde gösterir.
- Hangi serviste ne kadar gecikme olduğunu net görürsün.

**Fayda:**
> Sleuth log’ları üretir, Zipkin ise onları toplayıp sana “istek akışı haritası” çıkarır.

---

## 4️⃣ Prometheus

**Açıklama:**  
Zaman serisi (time-series) veri toplayan ve saklayan monitoring sistemidir.  
Genellikle **metrik toplamak** için kullanılır (CPU, bellek, istek sayısı, response time vb.).  
“Pull” yaklaşımı ile çalışır → belirlediğin endpoint’lerden (`/actuator/prometheus`) periyodik veri çeker.

**Ne işe yarar?**
- Servislerden, altyapıdan veya uygulamalardan metrik toplar.
- Metrikler üzerinde sorgu (PromQL) çalıştırabilirsin.

**Fayda:**
> Sistem sağlığı, performans ve kapasite planlaması için metrikleri toplar.

---

## 📌 Birbirleriyle İlişkisi

1. **Spring Cloud Sleuth**  
   → Her isteğe trace id/span id ekler, log’ları ve trace verilerini üretir.

2. **Zipkin**  
   → Sleuth’un ürettiği trace verilerini toplar, saklar ve timeline görünümünde sunar.

3. **Prometheus**  
   → Uygulamanın metriklerini (performans, istek sayısı, hata oranı) toplar.

4. **Grafana**  
   → Prometheus’tan metrikleri çeker, görselleştirir.  
   Zipkin’i de veri kaynağı olarak ekleyip trace görselleştirmesi yapabilir.

---

## 💡 İşini Nasıl Kolaylaştırır?

- **Sorun çözme süresi kısalır** → Trace + log + metrik bir arada olduğu için “Hangi servis yavaş?” sorusunu hızlıca cevaplayabilirsin.
- **Proaktif izleme** → Grafana’daki uyarılar ile bir sorun kullanıcıya ulaşmadan önce müdahale edebilirsin.
- **Kök neden analizi** → Sleuth + Zipkin ile isteğin nerede takıldığını saniyesine kadar görebilirsin.
- **Performans optimizasyonu** → Prometheus metrikleri ile darboğaz olan noktaları ölçebilirsin.



## 🔗 3. Birlikte Çalışma Örneği
Düşün ki bir mikroservis sisteminde yavaşlık var:

1. **Prometheus** → CPU kullanımının %90’a çıktığını, response time’ın arttığını gösterir.

2. **Grafana** → Bu metrikleri anlık dashboard’ta grafik olarak gösterir, alarm üretir.

3. **Zipkin** → Sorunlu isteğin hangi servislerde kaç ms harcadığını, hangi noktada geciktiğini gösterir.

## 💡 Kısacası:

- **Prometheus** = “Sayıları toplayan”

- **Zipkin** = “İstek yolculuğunu çizen”

- **Grafana** = “Bunları görselleştiren yönetim masası”


# 10. Spring Boot Uygulama Seviyesi Metric'leri Dışa Açma

## 📌 Nedir?
Spring Boot, **Micrometer** kütüphanesi üzerinden uygulama seviyesindeki metrikleri toplar.  
Bu metrikler:
- Request sayısı
- Response süreleri
- JVM bellek kullanımı
- GC istatistikleri
- Thread sayıları
- Custom metrikler

Toplanan bu metrikleri **Prometheus, Graphite, New Relic, Datadog** gibi sistemlere **Actuator endpoints** aracılığıyla dışa açabiliriz.

## Özet
> "Spring Boot’ta application-level metric'leri Actuator ile expose ederiz. Micrometer bu metrikleri toplar, ardından `/actuator/metrics` veya `/actuator/prometheus` gibi endpoint’lerle dışarı açarız. Prometheus, Datadog gibi sistemler bu endpoint’ten veriyi scrape eder."


# 11. Spring ve Spring Boot Farkı
| Özellik                    | Spring                               | Spring Boot                                     |
| -------------------------- | ------------------------------------ | ----------------------------------------------- |
| Konfigürasyon              | Manuel (XML veya Java)               | Otomatik yapılandırma (Auto Config)             |
| Başlangıç Zorluğu          | Daha karmaşık                        | Başlamak kolay ve hızlı                         |
| Web Sunucusu               | Harici olarak eklenmeli              | Yerleşik (embedded) Tomcat/Jetty ile gelir      |
| Bağımlılık Yönetimi        | Manuel                               | Starter dependency'ler ile otomatik             |
| Uygulama Geliştirme Süresi | Daha uzun                            | Daha kısa ve verimli                            |
| CLI Desteği                | Yok                                  | Spring Boot CLI ile komut satırından çalıştırma |
| Yapılandırma Dosyası       | XML/Java config                      | `application.properties` veya `application.yml` |
| Hedef Kullanım             | Esnek, modüler yapı isteyen projeler | Hızlı prototipleme ve mikroservis mimarileri    |


# 12. Spring IoC (Inversion of Control)

## 1. Tanım
- **IoC**, nesnelerin oluşturulması ve yönetilmesinin uygulama kodundan alınıp **Spring Container**'a verilmesidir.
- Nesneler **bean** olarak konteyner içinde tutulur.
- Nesnelerin bağımlılıkları Spring tarafından **dependency injection** ile sağlanır.

---

## 2. IoC Olmadan vs IoC ile

### IoC Olmadan
```java
public class OrderService {
    private PaymentService paymentService = new PaymentService(); // manuel bağımlılık
}
```

### IoC ile 
```java
@Service
public class OrderService {
    private final PaymentService paymentService;

    @Autowired
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService; // Spring enjekte ediyor
    }
}
```

# 13. Design Patterns

## Singleton

```java
public class DatabaseConnection {

    private DatabaseConnection() {

    }

    private static class DatabaseConnectionHolder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    public static DatabaseConnection getInstance() {
        return DatabaseConnectionHolder.INSTANCE;
    }
}
```

## Factory
### Tanım
Factory Pattern, nesne oluşturma işini merkezileştiren, nesne üretim sürecini new ile doğrudan yapmak yerine bir fabrika methodu üzerinden yapan design pattern’dir. 

### Ortak Interface
```java
public interface Payment {
    void pay(double amount);
}
```

### Concrete Class’lar
```java

public class CreditCardPayment implements Payment {
    public void pay(double amount) {
        System.out.println(amount + " TL kredi kartı ile ödendi.");
    }
}

public class PaypalPayment implements Payment {
    public void pay(double amount) {
        System.out.println(amount + " TL PayPal ile ödendi.");
    }
}

public class BankTransferPayment implements Payment {
    public void pay(double amount) {
        System.out.println(amount + " TL havale ile ödendi.");
    }
}

```

### Factory Class
```java
public class PaymentFactory {

    public static Payment getPaymentMethod(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Ödeme tipi boş olamaz");
        }
        switch (type.toLowerCase()) {
            case "creditcard":
                return new CreditCardPayment();
            case "paypal":
                return new PaypalPayment();
            case "banktransfer":
                return new BankTransferPayment();
            default:
                throw new IllegalArgumentException("Geçersiz ödeme tipi: " + type);
        }
    }
}
```

### Kullanım
```java
public class Main {
    public static void main(String[] args) {
        Payment payment = PaymentFactory.getPaymentMethod("paypal");
        payment.pay(150.0);

        Payment payment2 = PaymentFactory.getPaymentMethod("creditcard");
        payment2.pay(300.0);
    }
}
```

## Strategy
### Tanım
Değişebilen bir davranışı, çalışma zamanında (runtime) değiştirebilmektir.

### Strategy Interface

```java
public interface PaymentStrategy {
    void pay(double amount);
}
```

### Concrete Class’lar

```java
public class CreditCardPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println(amount + " TL kredi kartı ile ödendi.");
    }
}
public class PaypalPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println(amount + " TL PayPal ile ödendi.");
    }
}
```

### Context Class
```java
public class PaymentContext {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void pay(double amount) {
        paymentStrategy.pay(amount);
    }
}
``` 

### Kullanım
```java
public class Main {
    public static void main(String[] args) {
        PaymentContext paymentContext = new PaymentContext();
        paymentContext.setPaymentStrategy(new CreditCardPayment());
        paymentContext.pay(150.0);

        paymentContext.setPaymentStrategy(new PaypalPayment());
        paymentContext.pay(300.0);
    }
}
```

## Decorator
Decorator Pattern, mevcut bir nesnenin davranışını orijinal kodunu değiştirmeden genişletmemizi sağlar.
Bunu yapmak için nesneyi başka bir sınıfın içine “sarar” (wrap eder).

### Ana Interface
```java
public interface Coffee {
    String getDescription();
    double getCost();
}
```

### Concrete Component
```java 
public class SimpleCoffee implements Coffee {
    @Override
    public String getDescription() {
        return "Sade Kahve";
    }

    @Override
    public double getCost() {
        return 10.0;
    }
}

```

### Base Decorator
```java
public abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee; // Kompozisyon

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public String getDescription() {
        return coffee.getDescription();
    }

    @Override
    public double getCost() {
        return coffee.getCost();
    }
}

```

### Concrete Decorator’lar
```java
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Süt";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 2.0;
    }
}

public class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Şeker";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 1.0;
    }
}

```

### Kullanım
```java 
public class Main {
    public static void main(String[] args) {
        Coffee coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " -> " + coffee.getCost() + " TL");

        coffee = new MilkDecorator(coffee); // süt ekledik
        coffee = new SugarDecorator(coffee); // şeker ekledik

        System.out.println(coffee.getDescription() + " -> " + coffee.getCost() + " TL");
    }
}

```
### Avantajları
    ✅ Ana sınıfın koduna dokunmadan yeni özellik ekleyebilirsin.
    ✅ İstediğin kombinasyonu runtime’da oluşturabilirsin.
    ✅ Kalıtım patlamasını (subclass explosion) önler.

## Observer
### Tanım
Observer Pattern, bir nesnedeki değişiklikleri otomatik olarak ona bağlı diğer nesnelere bildirmek için kullanılan bir davranışsal (behavioral) design pattern’dir.

📌 Temel fikir:

    - Subject (veya Publisher) — durumu takip edilen nesne
    - Observer (veya Subscriber) — Subject’in değişikliklerini dinleyen nesneler
    - Subject değiştiğinde, kendisini dinleyen tüm Observer’lara haber verir.

1️⃣ Günlük Hayattan Örnek

    - Subject: Haber ajansı
    - Observer’lar: Gazeteler, TV kanalları, web siteleri
    - Ajans yeni bir haber yayınladığında, tüm abonelere (observer’lara) haber gider.
    - Ajansın hangi medyaları haberdar ettiğini bilmesine gerek yoktur. Observer’lar kendisi abone olur ve değişiklikleri otomatik alır.

## Builder

Özellikle:

    - Nesnenin çok sayıda parametresi varsa,
    - Bazıları opsiyonelse,
    - Karmaşık yapıda oluşturuluyorsa,
    - Constructor ile parametre sayısı artarsa okunabilirliği düşüyorsa
Builder Pattern devreye girer.

```java
public class User {
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String phone;
    private boolean newsletterSubscribed;

    private User(UserBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
        this.email = builder.email;
        this.phone = builder.phone;
        this.newsletterSubscribed = builder.newsletterSubscribed;
    }

    public static class UserBuilder {
        private String firstName;
        private String lastName;
        private int age;
        private String email;
        private String phone;
        private boolean newsletterSubscribed;

        public UserBuilder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public UserBuilder age(int age) {
            this.age = age;
            return this; // method chaining için
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder newsletterSubscribed(boolean subscribed) {
            this.newsletterSubscribed = subscribed;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User [firstName=" + firstName + ", lastName=" + lastName +
                ", age=" + age + ", email=" + email + ", phone=" + phone +
                ", newsletterSubscribed=" + newsletterSubscribed + "]";
    }
}

```

## Adapter
### Tanım
Adapter Pattern, uyumsuz (incompatible) arayüzlere sahip sınıfları, ortak bir arayüz üzerinden kullanılabilir hale getirmek için kullanılır.

```java
public interface SenderService {
    void send(String message);
}
```

```java

public class SendEmailService {
    public void sendEmail(String message, String email) {
        System.out.println("Sending email to " + email + " with message: " + message);
    }
}

```

```java
public class SendSmsService {
    public void sendSms(String message, String phoneNumber) {
        System.out.println("Sending SMS to " + phoneNumber + " with message: " + message);
    }
}
```
```java
public class SendSmsServiceAdapter implements SenderService {
    private final SendSmsService smsService;

    public SendSmsServiceAdapter(SendSmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void send(String message) {
        smsService.sendSms(message, "+90123456789");
    }
}
```

```java
public class SendEmailServiceAdapter implements SenderService {
    private final SendEmailService emailService;

    public SendEmailServiceAdapter(SendEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void send(String message) {
        emailService.sendEmail(message, "kadioglumf@gmail.com");
    }
}
```

```java
    public static void main(String[] args) {
        SenderService smsService = new SendSmsServiceAdapter(new SendSmsService());
        smsService.send("Sms message!");

        SenderService emailService = new SendEmailServiceAdapter(new SendEmailService());
        emailService.send("Email message!");
    }
```

# 14. Circuit Breaker
## 🎯 Circuit Breaker Nedir?
Circuit Breaker, yazılım sistemlerinde hatalı veya yavaş çalışan bir servise tekrar tekrar istek gönderip sistemi yormayı önleyen bir koruma mekanizmasıdır.

    Mantığı: Elektrikteki sigortaya benzer. Bir yerde arıza varsa, o hattı keser ve sistemi korur.

## 🧠 Neden Kullanılır?
    - Bağımlı olduğun servis yavaş veya yanıt vermiyor olabilir.
    - Sürekli istek göndermek sistemi gereksiz yere yorar.
    - Hataları erken fark edip başka bir davranış (fallback) sergileyebilirsin.

## 🔄 Çalışma Mantığı (Durumlar)
Circuit Breaker üç ana durumda çalışır:

    1. Closed (Kapalı) – Normal çalışır, istekler doğrudan hedef servise gider.
    2. Open (Açık) – Hedef servis çok hata verdiyse bağlantı kesilir, istekler hemen reddedilir.
    3. Half-Open (Yarı Açık) – Bir süre bekledikten sonra sınırlı sayıda istek gönderilir, eğer düzelmişse tekrar Closed durumuna geçer.

## Circuit Breaker ve Retry

| Özellik         | @CircuitBreaker                                   | @Retry                         |
| --------------- | ------------------------------------------------- | ------------------------------ |
| Odak noktası    | Sorunlu servisi geçici olarak devre dışı bırakmak | İsteği tekrar denemek          |
| Davranış        | “Hata oranı yüksek → devreyi aç”                  | “Hata aldıysan → tekrar dene”  |
| Kullanım zamanı | Servis ciddi şekilde arızalıysa                   | Servis ara sıra hata veriyorsa |
| Fallback        | Evet                                              | Evet                           |

## 🔄 Kullanım
```java
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {

    @Retry(name = "myService", fallbackMethod = "fallback")
    @CircuitBreaker(name = "myService", fallbackMethod = "fallback")
    public String callExternalApi() {
        System.out.println("API çağrısı yapılıyor...");
        if (Math.random() < 0.7) { // %70 hata simülasyonu
            throw new RuntimeException("Servis hatası!");
        }
        return "✅ Başarılı yanıt";
    }

    // Fallback metodu
    public String fallback(Throwable t) {
        return "⚠️ Servis şu anda kullanılamıyor, lütfen daha sonra tekrar deneyin.";
    }
}

```

# 15. SOLID

## S — Single Responsibility Principle (SRP)
Bir sınıfın yalnızca bir tane sorumluluğu olmalı, yani sadece bir işi yapmalı.
Değişiklik sebepleri tek bir konuya bağlı olmalı.

### Örnek:
Bir **`User`** sınıfı sadece kullanıcı bilgilerini tutmalı, kullanıcı doğrulama veya veri tabanı işlemleri başka sınıflarda olmalı.

### O — Open/Closed Principle (OCP)
Bir sınıf yeni özellikler eklemeye açık olmalı, ama mevcut kodu değiştirmeye kapalı olmalı.
Yani kodu değiştirmeden yeni davranışlar ekleyebilmeliyiz.

### Nasıl?
    - Kalıtım (inheritance) veya
    - Arayüzler (interfaces) kullanarak.

## L — Liskov Substitution Principle (LSP)

Bir sınıfın alt sınıfları, üst sınıfının yerine geçebilmeli ve programın davranışını bozmamalı.
Yani alt sınıflar, üst sınıfın sözleşmesini (contract) bozmadan genişletmeli.

### Bad Practice
```java
class Bird {
    public void fly() {
        System.out.println("Uçuyor");
    }
}

class Penguin extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguenler uçamaz!");
    }
}

public class Main {
    public static void main(String[] args) {
        Bird bird = new Penguin();
        bird.fly();  // Çalışma zamanı hatası!
    }
}

```

### Best Practice
```java
interface Bird {
    void move();
}

class FlyingBird implements Bird {
    @Override
    public void move() {
        System.out.println("Uçuyor");
    }
}

class Penguin implements Bird {
    @Override
    public void move() {
        System.out.println("Yüzüyor");
    }
}

public class Main {
    public static void main(String[] args) {
        Bird bird1 = new FlyingBird();
        Bird bird2 = new Penguin();

        bird1.move(); // Uçuyor
        bird2.move(); // Yüzüyor
    }
}

```
## I — Interface Segregation Principle (ISP)
Kullanıcıların ihtiyaç duymadığı büyük arayüzlere bağlı kalmaları zorunlu olmamalı.
Büyük arayüzler yerine, spesifik ve küçük arayüzler tercih edilmeli.

## D — Dependency Inversion Principle (DIP)

Yüksek seviyeli modüller, düşük seviyeli modüllere değil, soyutlamalara (arayüzler/abstract class) bağlı olmalı.

Soyutlamalar detaylara bağlı olmamalı; detaylar soyutlamalara bağlı olmalı.

### Bad Practice
```java
class MySQLDatabase {
    public void save(String data) {
        System.out.println("Veri MySQL'e kaydedildi: " + data);
    }
}

class UserService {
    private MySQLDatabase db = new MySQLDatabase();  // Direkt bağımlılık

    public void saveUser(String user) {
        db.save(user);
    }
}

```

### Best Practice
```java
interface Database {
    void save(String data);
}

class MySQLDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("Veri MySQL'e kaydedildi: " + data);
    }
}

class MongoDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("Veri MongoDB'ye kaydedildi: " + data);
    }
}

class UserService {
    private Database db;  // Soyutlamaya bağımlılık

    public UserService(Database db) {
        this.db = db;
    }

    public void saveUser(String user) {
        db.save(user);
    }
}

public class Main {
    public static void main(String[] args) {
        Database db = new MySQLDatabase();
        UserService userService = new UserService(db);

        userService.saveUser("Fatih");
    }
}

```

## Özet
| Harf | Prensip                         | Açıklama                                    |
| ---- | ------------------------------- | ------------------------------------------- |
| S    | Single Responsibility Principle | Bir sınıfın sadece bir sorumluluğu olsun    |
| O    | Open/Closed Principle           | Yeni özellik eklemek için kodu değiştirme   |
| L    | Liskov Substitution Principle   | Alt sınıflar üst sınıfın yerine geçebilmeli |
| I    | Interface Segregation Principle | Küçük, spesifik arayüzler kullanılmalı      |
| D    | Dependency Inversion Principle  | Bağımlılıklar soyutlamalara olmalı          |


# 16. Zookeeper'ın kafka alanındaki görevi nedir ?

## 📝 Özet
- Kafka’nın eski versiyonlarında ZooKeeper, broker’lar arası koordinasyon, metadata yönetimi ve leader seçiminden sorumludur. 2.8 sonrası KIP-500 ile KRaft mode’a geçildi ve ZooKeeper bağımlılığı kaldırılarak bu görevler Kafka Controller içine taşındı.

# 17. Kafka Poison Pill & Deserialize Exception Handling

## 📌 Nedir?
**Poison Pill** (Zehirli Hap), Kafka’da **deserialize edilemeyen** veya **işlenemeyen** bir mesajın consumer uygulamasını sürekli olarak hata döngüsüne sokması durumudur.

**Örnek:**
- Producer, beklenen şemadan farklı bir JSON gönderdi.
- Consumer, bu mesajı deserialization aşamasında okuyamaz.
- Consumer tekrar başladığında aynı mesajı okumaya çalışır ve yine hata alır.
- Sonuç: Consumer sürekli crash/restart döngüsüne girer.

---

## ⚠️ Tipik Nedenler
1. **Schema Değişiklikleri**
    - Producer ile Consumer farklı veri formatı kullanır.
2. **Bozuk Mesajlar**
    - Yanlış encoding / byte dizisi bozulması.
3. **Yanlış Serializer/Deserializer Kullanımı**
    - Producer’da Avro, Consumer’da JSON kullanmak gibi.
4. **Veri Tipi Uyuşmazlığı**
    - String beklerken int gelmesi.

---

## Çözüm
- Schema Registry + Avro/Protobuf Kullanma
- Hatalı mesajlar için dead letter topic kullanma


# 18. Kod kalitesini arttırmak için neler yapılabilir
- checkstyle
- continious integration
- tests
- code review 

# 19. Spring Boot Kafka Metrics Monitoring (Consumer Lag)

## 📌 Nedir?
**Consumer Lag**, Kafka’da **consumer’ın en son okuduğu offset** ile **partition’daki en son üretilen mesajın offset’i** arasındaki farktır.

> Lag = `latestOffset - committedOffset`

**Yüksek lag** = Consumer mesajları geriden takip ediyor (performans/bottleneck göstergesi).

# 20. Monolith ile Microservice arasındaki farklar nelerdir?
| Kriter                      | **Monolitik Mimari**                                            | **Mikroservis Mimari**                                                                       |
| --------------------------- | --------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| **Deploy**                  | Küçük bir değişiklikte bile tüm uygulama yeniden deploy edilir. | Sadece değişen servis yeniden deploy edilir.                                                 |
| **Teknoloji Esnekliği**     | Genelde tek bir teknoloji/dil kullanılır.                       | Servisler farklı diller, framework’ler, veritabanlarıyla yazılabilir.                        |
| **Bağımlılık**              | Katmanlar arası sıkı bağlılık vardır.                           | Servisler gevşek bağlıdır, API’ler aracılığıyla haberleşir.                                  |
| **Hata Yönetimi**           | Bir modüldeki hata tüm sistemi etkileyebilir.                   | Hata yalnızca ilgili servisi etkiler.                                                        |
| **Test & Debug**            | Daha kolaydır çünkü her şey tek yerde.                          | Daha zordur; servisler arası iletişim karmaşık olabilir.                                     |
| **Operasyonel Karmaşıklık** | Yönetimi ve izlenmesi kolaydır.                                 | Çok sayıda servis olduğundan monitoring, logging, service discovery gibi ek altyapı gerekir. |
| **Kullanım Alanı**          | Küçük/orta ölçekli projeler için uygundur.                      | Büyük, sürekli büyüyen ve farklı ekiplerin geliştirdiği projeler için uygundur.              |


# 21. Stack ve Heap

## Stack
- Küçük ve hızlı bir bellek alanıdır.
- Metod çağrıları sırasında kullanılır.
- Yerel değişkenler ve metot çağrı bilgileri burada tutulur.
- Bellek yönetimi otomatik yapılır (metot bittiğinde stack’teki veriler silinir).
- LIFO (Last In, First Out) mantığıyla çalışır.
- Primitive tipler (int, double, boolean vb.) veya nesne referansları stack’te saklanır.

## Heap
- Büyük ve yavaş bir bellek alanıdır.
- Nesneler (Objects) heap’te oluşturulur.
- Heap’teki nesnelere referanslar aracılığıyla ulaşılır (referans stack’te, nesne heap’te durur).
- Garbage Collector (Çöp Toplayıcı) kullanılmayan nesneleri temizler.
- Thread’ler arasında paylaşımlıdır.

## ✅ Özet:

- Stack: Hızlı, küçük, geçici (metot çağrısı bitince temizlenir).
- Heap: Büyük, nispeten yavaş, nesneler burada kalır, GC yönetir.

# 22. ACID
## Tanım:
- Veritabanı işlemlerinin güvenilirliğini garanti eden 4 özellik.

1. **Atomicity** - Bir işlem ya tamamen yapılır ya da hiç yapılmaz.
2. **Consistency** - Transaction öncesi ve sonrası, veritabanı tutarlı durumda olmalı.
3. **Isolation** - Aynı anda çalışan transaction’lar birbirini etkilememeli.
4. **Durability** - İşlem başarılı olduktan sonra, sistem çökse bile işlem kalıcı olmalı.


# 23. Spring'te Scope Türleri

| Scope           | Yaşam süresi                               | Kullanım Alanı          |
| --------------- | ------------------------------------------ | ----------------------- |
| **singleton**   | Tüm application context boyunca 1 instance | Genel servisler         |
| **prototype**   | Her kullanımda yeni instance               | State tutan objeler     |
| **request**     | Her HTTP request’te yeni instance          | Web request bazlı bean  |
| **session**     | Her HTTP session için 1 instance           | Kullanıcı bazlı veriler |
| **application** | Tüm ServletContext için 1 instance         | Global app verileri     |
| **websocket**   | Her WebSocket bağlantısı için 1 instance   | WebSocket oturumları    |


# 24. Java'da Polimorfizm (Polymorphism)

Bir nesnenin birden fazla formda davranabilmesini sağlar.  
Java’da polimorfizm **3 ana şekilde** uygulanır:

---

## Compile-time Polymorphism (Statik / Erken Bağlama)

Derleme anında hangi metodun çalışacağı **parametre imzasına göre** belirlenir.  
Java’da **method overloading** ile sağlanır.

###  Örnek
```java
class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }
}
```

## Runtime Polymorphism
Java’da method overriding ile sağlanır.
**@Override** anotasyonu kullanılır.


## Parametric Polymorphism
Java’da Generics ile uygulanır.

###  Örnek
```java
class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

public class Main {
    public static void main(String[] args) {
        Box<Integer> intBox = new Box<>();
        intBox.set(100);
        System.out.println(intBox.get());  // 100

        Box<String> strBox = new Box<>();
        strBox.set("Hello");
        System.out.println(strBox.get());  // Hello
    }
}
```


# 25. Kafka vs RabbitMq
| Özellik / Kriter              | **Apache Kafka**                                                                                                              | **RabbitMQ**                                                                                                                           |
| ----------------------------- | ----------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| **Mesaj Saklama**             | Mesajlar log’larda kalıcıdır (retention süresi boyunca diskte tutulur).                                                       | Mesajlar kuyrukta tüketilene kadar tutulur, varsayılan kalıcılık sınırlı.                                                              |
| **Tüketim Modeli**            | Consumer Group ile mesajlar birden fazla tüketiciye **paralel** dağıtılır. Aynı grup içindeki tüketiciler iş yükünü paylaşır. | Mesaj genellikle tek tüketiciye iletilir. Fanout gibi exchange tipleriyle broadcast yapılabilir ama log benzeri tekrar tüketim yoktur. |
| **Performans**                | Çok yüksek throughput (milyonlarca mesaj/sn). Büyük veri akışı ve event streaming için uygun.                                 | Daha düşük throughput. Düşük gecikme (low latency) gerektiren işler için optimize.                                                     |
| **Kullanım Senaryosu**        | Event streaming, log toplama, gerçek zamanlı analiz, mikroservisler arası event-driven mimari.                                | Görev dağıtımı (task queue), klasik mesajlaşma, RPC, kısa ömürlü mesaj senaryoları.                                                    |
| **Saklama Süresi**            | Belirlenen retention süresi boyunca saklanır, tüketilse bile silinmez (örneğin 7 gün).                                        | Tüketildikten sonra kuyruktan silinir.                                                                                                 |
| **Sıralama (Ordering)**       | Partition bazında sıralama garanti edilir. Global sıralama yoktur.                                                            | Queue bazında sıralama garanti edilir.                                                                                                 |
| **Dağıtık Ölçeklenebilirlik** | Yüksek seviyede dağıtık çalışır, yatayda kolayca ölçeklenir.                                                                  | Ölçekleme daha sınırlıdır, genellikle vertical scaling ile yönetilir.                                                                  |
| **Yönetim & İzleme**          | Zookeeper/Kraft ile cluster yönetimi gerekir. Ekstra monitoring araçları (Kafka Manager, Prometheus, Grafana).                | Yönetim konsolu (UI) dahili olarak gelir, monitoring kolaydır.                                                                         |
| **Protokol**                  | Özel Kafka protokolü (TCP tabanlı).                                                                                           | AMQP protokolü (genel, esnek, farklı dillerde desteklenir).                                                                            |
| **Mesaj Yeniden Okuma**       | İstenen offset’ten başlayarak eski mesajları tekrar okuyabilirsin.                                                            | Tüketilmiş mesaj tekrar okunamaz (ancak dead-letter veya requeue mekanizmasıyla tekrar gönderilebilir).                                |


# 26. Yorum soruları
## 1. Bir ekranda veri listelenecek. Tabloda çok fazla kayıt olabilir. servisin cevabı hızlı dönmesi için ne yaparsın?
### Cevap:
        - index koyulabilir
        - pagination eklenebilir
        - partion table yapısu kurulabilir
        - cache eklenebilir
## 2. Bir nesne initialize edildikten sonra hemen bir methodum çalışssın istiyorum. bunu nasıl yaparım?
### Cevap:
        - @PostConstruct annotation
## 3. Bir nesne destroy edilirken bir methodum çalışsın istiyorum. bunu nasıl yaparım?
### Cevap:
        - @PreDestroy annotation
## 4. Bir method var. bu method @Transactional ile işaretli. bu methodun içinde ilk satırda db save işlemi, ikinci satırda dış rest servise get isteği ama herhangi bir veri getirmiyor, üçüncü satırda ise yine bir db save işlemi var. sence bu tasarımda bir problem var mı ?


