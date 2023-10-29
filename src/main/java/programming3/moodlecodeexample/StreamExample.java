package programming3.moodlecodeexample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Example {
    public static void main(String[] args) {

        List < Person > names = new ArrayList < Person > ();
        names.add(new Person("Seppo", 20));
        names.add(new Person("Kalle", 24));
        names.add(new Person("Asko", 53));
        names.add(new Person("Mirja", 19));
        names.add(new Person("Terttu", 45));
        names.add(new Person("Anna", 76));

       
        int oldWayCount = 0;

        System.out.println("People in list:");
        for (Person person : names) {
            oldWayCount++;
            System.out.println(person.getName());
        }

        System.out.println("number of people in list " + oldWayCount);

       

        // Stream documentation: https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html


        List < String > namesFromStream = names.stream().map((Person) -> Person.getName()).collect(Collectors.toList());
        System.out.println("People in the list: " + namesFromStream);

        Long count = names.stream().count();
        System.out.println("Number of elemets: " +count);

       
        List < String > reverseNames = namesFromStream.stream().sorted((o1, o2) -> o2.compareTo(o1)).collect(Collectors.toList());
        System.out.println("Reverse sorted names: " + reverseNames);

        List < String > sortedNames = namesFromStream.stream().sorted().collect(Collectors.toList());
        System.out.println("Sorted names: " + sortedNames);

        names.stream().filter(Person -> Person.getAge() == 19).forEach(Person -> System.out.println("Person who is 19 years old: " + Person.getName()));

        Map < String, Integer > peopleAndAges = names.stream().collect(Collectors.toMap(p -> p.getName(), p -> p.getAge()));
        System.out.println(peopleAndAges);

        //But out of curiosity, similar things were in the previous example...
        //String text = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(),StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        //would it mean...

        List <String> originalReguestBody = new ArrayList <String> ();

        originalReguestBody.add("Text \n");
        originalReguestBody.add("Second text ");
        originalReguestBody.add("Third text... ");

        System.out.println("Before stream: " + originalReguestBody);

        String filteredReguestBody = originalReguestBody.stream().collect(Collectors.joining());

        System.out.println("After stream: " + filteredReguestBody);

        //or if using the original...
        System.out.println("with line change character: " + originalReguestBody.stream().collect(Collectors.joining("\n")));


        //Stream is for handling and iterating easily operate elements instead of building iterations
        //StreamReader or more specificially InputStreamReader in this case is to a class to read streams (or more specifically in InputStreamReader is to read bytes)
        //that masks conveniently the actual I/O so we don't care in code where it is coming from or going to (see also filereader, bufferedreader and similar in oracle's api documentation)
        //note that the inputstreamreader is casted as bufferedreader (where lines() returns Stream<String> and we have the ability to use all neat stream commands...)


        //but what was that monstrotiety in the getRequestURI...

        String url = "http://localhost:1024/coordinates?param=message";
       
        System.out.println(url.toString().split("\\?")[1].split("=")[1]);

        //it splits the string based on the "regex"
        //so for example if we change it...

        System.out.println(url.toString().split("\\?")[1]);
       
        //so it just a simple way to identify a certain portion of the string.
        //or better way to get ALL query params...
        /*
       
        query = httpExchange.getRequestURI().getQuery());

        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                }else{
                    result.put(entry[0], "");
                }
            }
         
        not checking null though...
        copyright and reference to StackOverflow by anon01 at https://stackoverflow.com/questions/11640025/how-to-obtain-the-query-string-in-a-get-with-java-httpserver-httpexchange


         */

    }
}

class Person{

    private String name;
    private int age;

    public Person(String name, int age){this.name=name;this.age=age;}

    public String getName(){return this.name;}

    public int getAge(){return this.age;};

}