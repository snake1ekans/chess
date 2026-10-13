# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoARiqfKR3y-P8gKoQ2oEVEB-rgeWKlaSC6k7AWtGYCqwYajM2SqOKjiHhw5hICamB6VASa+tU-oALJ2Q5cAos5GCuQgUYxnGhQ6RUom1Om+GjGMOaqHm8y1MMyxNNw+iGOYGHABu7wVo8MBNPofxmXRTYeRUb5doJwkEkSYDToZKBrKZAKkvVFTDvytQAGbihwU7KVyN40vuFTLmKEpurK8pupyphWeqRUwAAchAzAir4Tb9eoXn1bUPZ9tuKDHT5dRuttYB7T4nARSgsYKehnnlHFMDpoEBEpWl5kLWgO0wIN+27NVDGHaoV2drUIzjZ1FUaVcvXlCdABqlBIINljlVsOyWaqGoAJJoCA0AouAZXI4TvV1dd9RkxTxYoOA+OVQCz2vfGMWfcgqbfU4ACMf38gD0HM5TbPMMtFbQDTBMAg29EXX1t4DdqUC6igVHoG1xVzHOU0jjNwoAOra+iAaXnraAwJAMgAEJq7pjxwrUzVqGAADiUCCCgHAG5yazPpRiHoKStUMxJdQGYb6XGeRtsR8hqmo9dXlxbhiXx5y5lkWMFF2wXKtNp43h+P4XgoOgMRxIkNd141vhYKJgqgfUDTSBG-ERu0EbdD0cn2QUwzF6n71u7C+lFyn1Hp+57uUDHVBIjAWPa7j57z5HavlNDoojWHO96sbvKm+Uy4wD7TI23qMB20T1kwGTVAmkg65P9DsNry6dt3Qek2em6NGZOxDGHbmUUp6xQFthIWoskr-XzJLNA79kDriCoHFyJpVBlyhhrI6q8kRhyfmjQ+0gUDcHRHbU+8Rz4LhhlfYUlDqGGDIJAYKj9J4gM7i3U8ZAfD7hgfzEoYBag5zFrmFByxBH7kGr2BA8pGqMnXC0U8+DGKVwCFw-w2BxQan4miG+SoNDt3EjULuPt+5D3sEqceu9oqZ3KB5F4c94Kp1LtCZeH0Tob2xtvCeSEerOkugfQho45ocBPhRBh01mH1FvswMO3CkLP3Wm-D+X9J4-2If-VOgDwau1AbHWo4DomXigW9PmX10yIOzOLGRYxMkYPgMFHBCA8GQ0mhffkv915h2gEgAAXoHN0xTXH33iEM0ZDFaolL-g1NEPtTEYhCZ2dWJsBpJN8nIEA8Q1BrJ6Yws29QvY5ADH2ExOZ0kam2jAKMaIHLXLUMc6aeSuxnX7Lwxm20Vk5iqbzTOsCxG1AAKx4SkalJp9yyDABoOuf5ahNG5IWevJFuzsDDAxJ-FYbxhZ4oGOEFYnJ1mLK8q4jIYAfDvCRaoDEGLgDYCjj43++k7E5jcRylAJNpAFmFuEYIgQQSbHiDrOWKkxirEymMZIoA1QSvSlKkE3LNpKkWFCDOscs5wPETASRoxuWjhgOMblvL+WCuFcsUV4qiJKulfbWVCB5WKr2N8VV6qpUXG6RXZi-gOAAHY3BOBQE4GIEZghwC4gANjaUeQwSKYBFF1R3RmjRWgdFsfY6YjiCIeqMhZHSLifFcqVGqgtaE1o2QCg4IwW5o7eVKTAfy8kHDlpQICpx2qQWpgSlCiWMrsrBgMDAfKsBCqJvbZo+ZfjN440sHQu2ZLRCbN6fSKJMTLxxMvtfJJUzUnoFua-NBWSD2FFRSdABO0gEMR+aU8pkDowvWgTU3VaYRb9qaS0z+caQoEFwSiiJ-SXRTJmWMgYEyS2DO1rMpeM9fGhNqEkpFDKy1KmXSoVdjDPYoEJN7FDwwkXtu3X0hJ8ADkgA1DjF5DzrQ5A0FWmjgYzTykefRt5psPm1AAIRfOKZ3JFbGwCqE7SIuKfakGNMTmMRNzHgzmlozaQDWyiFopA3x8hETaiWx1ihoj6GOOkevjp62m0UCbBeZB+DSGlS8rg1+NlccZO2b5YvV9oKDXjCRea+oPqmJV0sFQhAEALOxCQAkMAgW+whZgAAKQgOKGj-g5WUeTWI1NTbmjMhkj0blDiPFISzNgZ1gWoBwAgMFqAawzXSC1TUaeX4XjFeAKV8rlXqsuauIx2yrbHBaHkPZuEXkOyWObTW+wPJRPud7ZCyT0ilVDtyqOjABUYDDGyi1jCbWFYKAlIm813SZ2If8VvBdQS96aZU5E4+l46EkaOmRvdKSn6Me-dktJF7ENXvukUu9liH2VKfTzLt9We3wLqZ+6Tb3f0dK6Y2AhV3gNdlAzB8DtAQGuOgyMwOg2V5qa7PF8UKHmutYq9ADrcxeUYbCYfZDLnUOU+kPdphy5znMETUexN0gIBqDQAAcmYEJwzqmTpEevb90JjmbNzEKY9DgU3gWiJm79Ob0LpOTpBje5Ta6YZcaRa+SX+OVxrjKiVjCjtVDBawIxsgVvLksZgGKPsOO-vPCaBAeyUVmTTHfqcXQ3AFfdqV9hdMmZVcSyymb2AtuQZyerI7043AIbw-3ofGAJmUBIuFyz4U6eraGFIanB2EB1tR-gGT6PVv+OM0J2gORI4xNvs88lKT5l6-8gUX2eUtfA4vM0b6quXgWv13C43f28pEDBnHdgYrhA8gFCTeYxto3u6937oPXoxg1tJVXLZNA5WZjHE4OZPaUAVCsKZPMOrGEPwlpNWMXf4oD9H80mMU-5+qGX4bB5e8ufJ94B5DWQAG4k0G1O5yAhERxfIpNgDG9QV9U8Jt8Gl5tzIeQ9AR139DB-8uF291BO8lEGw0tzAMsV8e4+4B4h4t8CJH998IBD9Hg29gBBoUAL90RtJrpBRJlxgaDn8GDoI4VmDWCr9ccEM4YlACAaAKB5ICg4AKM1QjlLsdcVxwAkBJD+QFJZC2Z5DmdTknsVCaAioGNiYNpsgYAQAaUUQMAlEYBEsR43pPs4YAx9CUAFoABeR+KAVIavJtcQ1QlwgYc2PANAByOAwWIsEsGACFXOMYdoA5FEGwgUcw1mKw+UQqN4GAYLewVRGwtAFwJYTAmQT-dEAAfkINVh-2G2vmwMAOJBAJEKlwgP3GgPm1gOm3gRziQJbxQOgjQKW2wN71wNUHwMsHwSAA

