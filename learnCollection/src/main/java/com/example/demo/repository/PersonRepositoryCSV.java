package com.example.demo.repository;


import com.example.demo.model.Person;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Functions;
import com.google.common.collect.Ordering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PersonRepositoryCSV implements PersonRepositoryInterface {
    private ArrayList<Person> people;

    @Autowired
    public PersonRepositoryCSV(@Value("${csvFile}") String csvFile) {
        people = new ArrayList<>();
        loadData(csvFile);
    }

    private void loadData(String csvFile) {
        try {
            File file = ResourceUtils.getFile("classpath:static/" + csvFile);
            CsvMapper mapper = new CsvMapper(); // Dùng để ánh xạ cột trong CSV với từng trường trong POJO
            CsvSchema schema = CsvSchema.emptySchema().withHeader(); // Dòng đầu tiên sử dụng làm Header
            ObjectReader oReader = mapper.readerFor(Person.class).with(schema); // Cấu hình bộ đọc CSV phù hợp với kiểu
            Reader reader = new FileReader(file);
            MappingIterator<Person> mi = oReader.readValues(reader); // Iterator đọc từng dòng trong file
            while (mi.hasNext()) {
                Person person = mi.next();
                people.add(person);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public List<Person> getAll() {
        return people;
    }

    @Override
    public List<Person> sortPeopleByFullNameReversed() {
        return people.stream().sorted(Comparator.comparing(Person::getFullname).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<String> getSortedCities() {
        /*
         * return people.stream(). sorted(Comparator.comparing(Person::getCity)).
         * map(Person::getCity).collect(Collectors.toList());
         */
        return people.stream().map(Person::getCity).sorted().collect(Collectors.toList());
    }

    @Override
    public List<String> getSortedJobs() {
        return people.stream().map(Person::getJob).sorted().collect(Collectors.toList());
    }



    @Override
    public HashMap<String, Integer> findTop5Cities() {
        HashMap<String, List<Person>> rs = groupPeopleByCity();
        Map<String, Integer> map = new HashMap<>();
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (String key:rs.keySet()){
            if(rs.containsKey(key)){
                map.put(key, rs.get(key).size());
            }
        }
        // Sap xep theo thu tu giam dan
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        // Lay 5 phan tu dau tien
        int limit = 5;
        int count = 0;
        Iterator<Integer> it = sortedMap.values().iterator();
        while (it.hasNext()){
            it.next();
            count++;
            if(count > limit){
                it.remove();
            }
        }

        return sortedMap;
    }

    @Override
    public HashMap<String, List<Person>> groupPeopleByCity() {

        Map<String, List<Person>> personByCity  = new HashMap<>();
        personByCity  = people.stream().collect(Collectors.groupingBy(Person::getCity));
        return (HashMap<String, List<Person>>) personByCity;
    }

    @Override
    public HashMap<String, Integer> findTop5Jobs() {
        HashMap<String, Integer> rs = groupJobByCount();
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        rs.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        // Lay 5 phan tu dau tien
        int limit = 5;
        int count = 0;
        Iterator<Integer> it = sortedMap.values().iterator();
        while (it.hasNext()){
            it.next();
            count++;
            if(count > limit){
                it.remove();
            }
        }
        return sortedMap;
    }

    @Override
    public HashMap<String, String> findTopJobInCity() {
//        Map<String, List<Person>> personByCity  = new HashMap<>();
//        personByCity  = people.stream().collect(Collectors.groupingBy(Person::getCity));
//        System.out.println("----");
//        System.out.println(personByCity);
//        Map<String, List<String>> cityAndlistJob = new HashMap<>();
//        for (String city:personByCity.keySet()){
//            if(cityAndlistJob.containsKey(city)){
//                Iterator<List<Person>> iterator = personByCity.values().iterator();
//                int n = 0;
//                while (n < personByCity.get(city).size()){
//                    cityAndlistJob.get(city).add(personByCity.get(city).get(n).getJob());
//                    n++;
//                }
//
//            }else{
//                List<String> list = new ArrayList<>();
//                Iterator<List<Person>> iterator = personByCity.values().iterator();
//                int n = 0;
//                while (n < personByCity.get(city).size()){
//                    list.add(personByCity.get(city).get(n).getJob());
//                    n++;
//                }
//                cityAndlistJob.put(city,list);
//            }
//        }
//        System.out.println("----");
//        System.out.println(cityAndlistJob);
//        // liet ke listJob
//        List<String> listJob = new ArrayList<>();
//        for (String job:groupJobByCount().keySet()){
//            listJob.add(job);
//        }
//        System.out.println(listJob);
//
//        // Dem trong thanh pho, so nguoi lam job
//        HashMap<String, Integer> rs = new HashMap<>();
//        for (String city:cityAndlistJob.keySet()){
//            Iterator<List<String>> iterator = cityAndlistJob.values().iterator();
//            int n = 0;
//            while (n < cityAndlistJob.get(city).size()){
//
//                n++;
//            }
//        }
        return null;
    }


    @Override
    public HashMap<String, Integer> groupJobByCount() {
        Map<String, List<Person>> personByJob  = new HashMap<>();
        personByJob  = people.stream().collect(Collectors.groupingBy(Person::getJob));
        HashMap<String, Integer> rs = new HashMap<>();

        for (String key:personByJob.keySet()){
            if(personByJob.containsKey(key)){
                rs.put(key, personByJob.get(key).size());
            }
        }
        return rs;
    }


    @Override
    public HashMap<String, Float> averageCityAge() {
        Map<String, List<Person>> personByAge = new HashMap<>();
        personByAge = people.stream().collect(Collectors.groupingBy(Person::getCity));

        HashMap<String, Float> cityAndAge = new HashMap<>();
        Calendar dob =  Calendar.getInstance();
        // Convert YYYY/MM/DD to Age
        for (String key:personByAge.keySet()){
            if(!cityAndAge.containsKey(key)){
                float sumAge = 0;
                int n = 0;
                while (n < personByAge.get(key).size()){
                    dob.setTime(personByAge.get(key).get(n).getBirthday());
                    try {
                        float age = getAge(dob);
                        sumAge = sumAge + age;
                        n++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                cityAndAge.put(key, (float) sumAge/personByAge.get(key).size());
            }
        }
        return cityAndAge;
    }

    @Override
    public HashMap<String, Float> averageJobAge() {
        Map<String, List<Person>> personbyJob = new HashMap<>();
        personbyJob = people.stream().collect(Collectors.groupingBy(Person::getJob));
        HashMap<String, Float> jobAndAge = new HashMap<>();
        Calendar dob =  Calendar.getInstance();
        // Convert YYYY/MM/DD to Age
        for (String key:personbyJob.keySet()){
            if(!jobAndAge.containsKey(key)){
                float sumAge = 0;
                int n = 0;
                while (n < personbyJob.get(key).size()){
                    dob.setTime(personbyJob.get(key).get(n).getBirthday());
                    try {
                        float age = getAge(dob);
                        sumAge = sumAge + age;
                        n++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                jobAndAge.put(key, (float) sumAge/personbyJob.get(key).size());
            }
        }
        return jobAndAge;
    }

    // Returns age given the date of birth
    public int getAge(Calendar dob) throws Exception {
        Calendar today = Calendar.getInstance();

        int curYear = today.get(Calendar.YEAR);
        int dobYear = dob.get(Calendar.YEAR);

        int age = curYear - dobYear;

        // if dob is month or day is behind today's month or day
        // reduce age by 1
        int curMonth = today.get(Calendar.MONTH);
        int dobMonth = dob.get(Calendar.MONTH);
        if (dobMonth > curMonth) { // this year can't be counted!
            age--;
        } else if (dobMonth == curMonth) { // same month? check for day
            int curDay = today.get(Calendar.DAY_OF_MONTH);
            int dobDay = dob.get(Calendar.DAY_OF_MONTH);
            if (dobDay > curDay) { // this year can't be counted!
                age--;
            }
        }

        return age;
    }

    @Override
    public HashMap<String, Float> averageJobSalary() {
        Map<String, List<Person>> personByJob  = new HashMap<>();
        personByJob  = people.stream().collect(Collectors.groupingBy(Person::getJob));
        HashMap<String, Float> jobAndSalary = new HashMap<>();

        for (String key:personByJob.keySet()){
            if(!jobAndSalary.containsKey(key)){
                float sum = 0;
                int n = 0;
                while (n < personByJob.get(key).size()){
                      sum = sum + (float) personByJob.get(key).get(n).getSalary();
                      n++;
                }
                jobAndSalary.put(key, (float) sum/personByJob.get(key).size());
            }
        }

        return jobAndSalary;
    }


    @Override
    public List<String> find5CitiesHaveMostSpecificJob(String job) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<String, Float> top5HighestSalaryCities() {
        HashMap<String, Float> averageJobSalary = averageJobSalary();

        HashMap<String, Float> result = averageJobSalary.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        // Lay 5 phan tu dau tien
        int limit = 5;
        int count = 0;
        Iterator<Float> it = result.values().iterator();
        while (it.hasNext()){
            it.next();
            count++;
            if(count > limit){
                it.remove();
            }
        }
        return result;
    }
}

