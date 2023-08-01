# littlepayboarding App 
`Done by Anthony John Garcia`

## Assumptions

- All Taps (except first and last) will be checked against the previous Tap.
- A max of 2 missed Taps can be assumed by the logic
- If Rider forgets to Tap OFF on Trip A then forgets to TAP ON on Trip B on the SAME Bus a check on Max Bus Ride Hours will be done, 
if it exceeds the it is assumed there are 2 incomplete rides.
- Double tap ON on the same stop will ignore the previous tap and not marked as cancelled.

## Business Logic
(PREVIOUS ACTION) -> (CURRENT ACTION)

```
ON -> OFF
	----> (SAME STOP) ON -> OFF [CANCELLED]
	----> (SAME BUS, DIFFERENT STOP, EXCEEDS MAX_BUS_RIDE_HOURS) ON -> MISSED(OFF) -> MISSED(ON) -> OFF [2X INCOMPLETE]
	----> (SAME BUS, DIFFERENT STOP) ON -> OFF  [COMPLETED]
	----> (DIFFERENT BUS) ON -> MISSED(OFF) -> MISSED(ON) -> OFF [2X INCOMPLETE]
OFF -> OFF
	----> OFF -> MISSED(ON) -> OFF [INCOMPLETE]
ON -> ON
	----> (DIFFERENT STOP) ON -> MISSED(OFF) -> ON [INCOMPLETE]
OFF -> ON 
	----> (LAST RECORD) OFF -> ON [INCOMPLETE]
	----> OFF -> ON [SKIPPED]
```

## Compiling littlepayboarding App

To compile littlepayboarding application, you can use the `javac` command, which is the Java compiler provided by the Java Development Kit (JDK). Here's a step-by-step guide:

1. **Install JDK**: If you haven't already, make sure you have the JDK installed on your system. You can download it from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

2. **Go into the project folder**: 
`cd littlepayboarding`

3. **Open terminal or command prompt**

4. **Compile the code**: 
`
javac ./src/*.java
`
5. **Run the code**: 
    a. *Without file input*:
``
java -cp ./src App
``
    b. *With file input*:
``
javac ./src/*.java && java -cp ./src App -p 'prices.csv' -i 'taps.csv' -o 'trips.csv'
``