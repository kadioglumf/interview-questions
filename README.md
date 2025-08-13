## Spring ve Spring Boot Farkı
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

## Design Patterns

### Singleton

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

### Factory
#### Tanım
Factory Pattern, nesne oluşturma işini merkezileştiren, nesne üretim sürecini new ile doğrudan yapmak yerine bir fabrika methodu üzerinden yapan design pattern’dir. 

#### Ortak Interface
```java
public interface Payment {
    void pay(double amount);
}
```

#### Concrete Class’lar
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

#### Factory Class
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

#### Kullanım
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

### Strategy
#### Tanım
Değişebilen bir davranışı, çalışma zamanında (runtime) değiştirebilmektir.

#### Strategy Interface

```java
public interface PaymentStrategy {
    void pay(double amount);
}
```

#### Concrete Class’lar

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

#### Context Class
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

#### Kullanım
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

### Decorator
Decorator Pattern, mevcut bir nesnenin davranışını orijinal kodunu değiştirmeden genişletmemizi sağlar.
Bunu yapmak için nesneyi başka bir sınıfın içine “sarar” (wrap eder).

#### Ana Interface
```java
public interface Coffee {
    String getDescription();
    double getCost();
}
```

#### Concrete Component
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

#### Base Decorator
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

#### Concrete Decorator’lar
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

#### Kullanım
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
#### Avantajları
    ✅ Ana sınıfın koduna dokunmadan yeni özellik ekleyebilirsin.
    ✅ İstediğin kombinasyonu runtime’da oluşturabilirsin.
    ✅ Kalıtım patlamasını (subclass explosion) önler.

### Observer
#### Tanım
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

### Builder

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

### Adapter

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

## Circuit Breaker
### 🎯 Circuit Breaker Nedir?
Circuit Breaker, yazılım sistemlerinde hatalı veya yavaş çalışan bir servise tekrar tekrar istek gönderip sistemi yormayı önleyen bir koruma mekanizmasıdır.

    Mantığı: Elektrikteki sigortaya benzer. Bir yerde arıza varsa, o hattı keser ve sistemi korur.

### 🧠 Neden Kullanılır?
    - Bağımlı olduğun servis yavaş veya yanıt vermiyor olabilir.
    - Sürekli istek göndermek sistemi gereksiz yere yorar.
    - Hataları erken fark edip başka bir davranış (fallback) sergileyebilirsin.

### 🔄 Çalışma Mantığı (Durumlar)
Circuit Breaker üç ana durumda çalışır:

    1. Closed (Kapalı) – Normal çalışır, istekler doğrudan hedef servise gider.
    2. Open (Açık) – Hedef servis çok hata verdiyse bağlantı kesilir, istekler hemen reddedilir.
    3. Half-Open (Yarı Açık) – Bir süre bekledikten sonra sınırlı sayıda istek gönderilir, eğer düzelmişse tekrar Closed durumuna geçer.

### Circuit Breaker ve Retry

| Özellik         | @CircuitBreaker                                   | @Retry                         |
| --------------- | ------------------------------------------------- | ------------------------------ |
| Odak noktası    | Sorunlu servisi geçici olarak devre dışı bırakmak | İsteği tekrar denemek          |
| Davranış        | “Hata oranı yüksek → devreyi aç”                  | “Hata aldıysan → tekrar dene”  |
| Kullanım zamanı | Servis ciddi şekilde arızalıysa                   | Servis ara sıra hata veriyorsa |
| Fallback        | Evet                                              | Evet                           |

### 🔄 Kullanım
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

## SOLID

### S — Single Responsibility Principle (SRP)
Bir sınıfın yalnızca bir tane sorumluluğu olmalı, yani sadece bir işi yapmalı.
Değişiklik sebepleri tek bir konuya bağlı olmalı.

#### Örnek:
Bir **`User`** sınıfı sadece kullanıcı bilgilerini tutmalı, kullanıcı doğrulama veya veri tabanı işlemleri başka sınıflarda olmalı.

#### O — Open/Closed Principle (OCP)
Bir sınıf yeni özellikler eklemeye açık olmalı, ama mevcut kodu değiştirmeye kapalı olmalı.
Yani kodu değiştirmeden yeni davranışlar ekleyebilmeliyiz.

#### Nasıl?
    - Kalıtım (inheritance) veya
    - Arayüzler (interfaces) kullanarak.

### L — Liskov Substitution Principle (LSP)

Bir sınıfın alt sınıfları, üst sınıfının yerine geçebilmeli ve programın davranışını bozmamalı.
Yani alt sınıflar, üst sınıfın sözleşmesini (contract) bozmadan genişletmeli.

#### Bad Practice
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

#### Best Practice
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
### I — Interface Segregation Principle (ISP)
Kullanıcıların ihtiyaç duymadığı büyük arayüzlere bağlı kalmaları zorunlu olmamalı.
Büyük arayüzler yerine, spesifik ve küçük arayüzler tercih edilmeli.

### D — Dependency Inversion Principle (DIP)

Yüksek seviyeli modüller, düşük seviyeli modüllere değil, soyutlamalara (arayüzler/abstract class) bağlı olmalı.

Soyutlamalar detaylara bağlı olmamalı; detaylar soyutlamalara bağlı olmalı.

#### Bad Practice
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

#### Best Practice
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

### Özet
| Harf | Prensip                         | Açıklama                                    |
| ---- | ------------------------------- | ------------------------------------------- |
| S    | Single Responsibility Principle | Bir sınıfın sadece bir sorumluluğu olsun    |
| O    | Open/Closed Principle           | Yeni özellik eklemek için kodu değiştirme   |
| L    | Liskov Substitution Principle   | Alt sınıflar üst sınıfın yerine geçebilmeli |
| I    | Interface Segregation Principle | Küçük, spesifik arayüzler kullanılmalı      |
| D    | Dependency Inversion Principle  | Bağımlılıklar soyutlamalara olmalı          |
