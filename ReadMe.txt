---Introduction---
Hello, and thank you for reviewing my tech test. This was developed and run with Intellij IDEA.

A summary of this solution is that it's not idempotent, and it is stateless. As it is, successive reruns will repeat
round-up calculations and continually add the result to the first savings goal it finds. It will also create one
if no savings goal exists. One imagines a use-case where a customer wants to repeatedly run a round-up calculation for
historic transactions despite having already done so - and this satisfies both that use case and the assessment specification.

If I were to expand on this solution I'd hold persistent state of all previous transactions calculated, so that the round-up
operation is idempotent per settled transaction. A "force calculation" flag could be included to allow for repeated processing of
historic transactions. Holding an index for the transaction IDs (to enable idempotent processing) will incur a cost, of course.

To simplify auth, the token from the client is passed through to the Starling API. I would've liked to do something smart
with Spring Security (e.g. SecurityContextHolder.getContext().getAuthentication().xyz) but its been a while since I last
dove into Spring Security and my understanding is that this project doesn't have to be perfect.

I admit the logging may be a bit much, but it can be useful to help diagnose production incidents. It does of course incur
a cost at scale (more computation, network traffic, storage, etc).

To expedite things I omitted a complete battery of unit tests, though I would normally write them for happy-path inputs/outputs.
The integration test was behaving strangely too. I'm used to using @MockBean, but I've since discovered that it's deprecated
in favour of @MockitoBean - which just would not work no matter what I tried. My workaround is to use @MockitoSpyBean (which
works for some reason???). If I had the time/capacity, I'd debug into the Spring Boot test runner itself and try figure out
exactly what's going on because Google/ChatGPT wasn't helpful.


---Task/Assessment Specification---
For a customer, take all the transactions in a given week and round them up to the nearest
pound. For example with spending of £4.35, £5.20 and £0.87, the round-up would be £1.58.
This amount should then be transferred into a savings goal, helping the customer save for
future adventures.


---Extra Tech Notes---
Upgraded built-in gradle to 8.14 for compatibility with Java 24 (just because it's the latest JDK).
    I had to switch to Java 23 to perform the upgrade as Gradle 8.13 included by Intellij is not compatible with Java 24,
    so './gradlew wrapper --gradle-version 8.14' was failing. SDKMan is my go-to for quickly switching Java distributions.
    Historically I use Amazon's Corretto for licensing reasons from previous companies, and it seems to just work most of
    the time. However, I have experienced at least one bug in a Corretto distribution during my career.