1. Considering Normalization and Denormalization in relational databases, which one of the followings is False?
    - More disk space is used in Denormalization compared to Normalization.
    - ✅ Cevap: `Normalization reduces the number of tables and joins.`
    - Denormalization helps to query data faster.
    - Normalization reduces inconsistency.
    - Denormalization introduces redundancy.


2. 
```java
    int process(int n)
{
    int count = 0;
    for (int i = n; i > 0; i /= 2) {       // dış döngü
        for (int j = 0; j < i; j++) {      // iç döngü
            count += 1;
        }
    }
    return count;
}
```
Cevap : O(n)

3. Which one of the following is not true about CI/CD (Continuous integration and Continuous delivery)?

    - CI/CD ensures that fault isolations are faster to detect and easier to implement
    - CI/CD enables fast feature introduction and fast turn-around on feature changes
    - Upgrades introduce smaller units of change and are less disruptive
    - ✅ Cevap: `CI/CD increases the Mean Time To Resolution (MTTR)`
    - CI/CD ensures that release cycles are shorter and targeted

4. Which one of the following is wrong about Horizontal Scaling vs Vertical Scaling?

    - Horizontal scaling means scaling by adding more machines to your pool of resources
    - Vertical scaling refers to scaling by adding more power (e.g. CPU, RAM) to an existing machine (also described as “scaling up”)
    - Single point of hardware failure which can cause bigger outages is more likely to happen in Vertical Scaling
    - ✅ Cevap: `Vertical scaling requires less downtime while extending the service capabilities`
    - Horizontal scaling requires a load-balancer to distribute load among the servers

5. Major benefits of using a Source Code Version Control System like Git, SVN, etc.:
   - **Collaboration** – Multiple developers can work on the same codebase simultaneously without overwriting each other’s changes.
   - **Version history** – Every change is recorded with details (who changed what and when), allowing easy rollback to previous versions if needed.
   - **Branching and merging** – Developers can experiment in isolated branches and later merge changes into the main codebase safely.
   - **Code backup and recovery** – The repository serves as a backup of the entire project, reducing the risk of data loss.
   - **Traceability** – Changes are linked to commits, issues, or features, providing clear traceability and accountability.
   - **Continuous integration support** – Works seamlessly with CI/CD pipelines to automate testing, building, and deployment.

6. Could you explain what atomicity property of a database means?
   - Atomicity, ACID özelliklerinden biridir ve bir veritabanı işlemindeki tüm adımların ya tamamen gerçekleşmesi ya da hiç gerçekleşmemesi gerektiğini ifade eder. Eğer işlem sırasında bir hata olursa, yapılan tüm değişiklikler geri alınır (rollback) ve veritabanı eski tutarlı haline döner.

7. Defining public, protected and private methods / variables for objects, describes which principle of Object-Oriented Programming?

   - Abstraction
   - Inheritance
   - Polymorphism
   - ✅ Cevap: `Encapsulation`
   - All of them

8. Which one of the following is wrong for differences between Agile Methodology vs Waterfall Methodology?
   - Agile is an incremental and iterative approach; Waterfall is a linear and sequential approach

   - Agile separates a project into sprints; Waterfall divides a project into phases

   - ✅ Cevap: `Waterfall allows changes in project development requirement whereas Agile has no scope of changing the requirements once the project development starts`

   - Testing is performed concurrently with development in Agile; testing phase comes after the build phase in Waterfall

   - In Waterfall software development is completed as one single project whereas Agile can be considered as a set of many different projects

9. Which one of the following best describes the Adapter Pattern?

   - Makes sure that only a single instance of a class is created → (Singleton Pattern)

   - Allows to access the elements of a collection object in sequential manner without any need to know underlying structure → (Iterator Pattern)

   - ✅ Cevap: `Introduces new classes to allow existing incompatible interfaces to communicate`

   - Allows to create an object using a common interface without exposing the creation logic → (Factory Pattern)

   - Adds a new functionality to an existing object without altering its structure → (Decorator Pattern)

10. Which one of the following is not an actual benefit of writing Unit Tests during your development?

   -Improves code coverage during automated tests

   - Improves the code quality

   - Debugging process can be simplified 

   - Enables to find bugs easily

   - ✅ Cevap: `Decreases the run time of your functions`









