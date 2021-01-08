package com.example.demo;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	PersonRepositoryInterface personRepository;

	@Test
	public void getAll() {
		List<Person> people = personRepository.getAll();
		assertThat(people).hasSize(20);
	}

	@Test
	public void getSortedCities(){
		List<String> sortedCities = personRepository.getSortedCities();
		sortedCities.forEach(System.out::println);  //In theo tất các thành phố ra để kiểm tra xem có sắp xếp không
	/*
		Cách này viết dài
		assertThat(sortedCities).contains("Paris", "Dubai");
		assertThat(sortedCities).isSortedAccordingTo(Comparator.naturalOrder());*/

		//Cách này chain các điều kiện test với nhau ngắn gọn và đẹp hơn
		assertThat(sortedCities).isSortedAccordingTo(Comparator.naturalOrder())
				.contains("Berlin", "Budapest", "Buenos Aires", "Copenhagen", "Hanoi", "Jakarta","Mexico City","Zagreb");
	}

	@Test
	public void getSortedJobs(){
		List<String> sortedJobs = personRepository.getSortedJobs();
		sortedJobs.forEach(System.out::println);

		assertThat(sortedJobs).isSortedAccordingTo(Comparator.naturalOrder())
				.contains("Pole Dancer", "Bartender", "Developer", "Personal Trainer", "Soldier", "Teacher", "Taxi Driver", "Nurse", "Musician");

	}

	@Test
	public void sortPeopleByFullNameReversed() {
		List<Person> sortedPeople = personRepository.sortPeopleByFullNameReversed();
		sortedPeople.forEach(person -> System.out.println(person.getFullname()));
		assertThat(sortedPeople).isSortedAccordingTo(Comparator.comparing(Person::getFullname).reversed());
	}

	@Test
	public void findTop5CitiesTest(){
		HashMap<String, Integer> rs = personRepository.findTop5Cities();
		assertThat(rs).hasSize(5);
	}

	@Test
	public void groupPeopleByCity(){
		HashMap<String, List<Person>> rs = personRepository.groupPeopleByCity();

		Collection<List<Person>> list = rs.values();
		assertThat(rs).containsKeys("Hanoi");
		assertThat(rs).hasSizeGreaterThan(10);
	}

	@Test
	public void groupJobByCount(){
		HashMap<String, Integer> rs = personRepository.groupJobByCount();
		assertThat(rs).hasSize(13);
		assertThat(rs).containsKeys("Film Maker").containsValues(5);
		assertThat(rs).containsKeys("Teacher").containsValues(3);
	}

	@Test
	public void findTop5Cities(){
		HashMap<String, Integer> rs = personRepository.findTop5Cities();
		assertThat(rs).hasSize(5);
		assertThat(rs).containsKeys("Havana").containsValues(3);
		assertThat(rs).containsKeys("Berlin").containsValues(2);
	}

	@Test
	public void findTop5Jobs(){
		HashMap<String, Integer> rs = personRepository.findTop5Jobs();
		assertThat(rs).hasSize(5);
		assertThat(rs).containsKeys("Film Maker").containsValues(5);
		assertThat(rs).containsKeys("Teacher").containsValues(3);
	}

	@Test
	public void findTopJobInCity(){
		HashMap<String, String> rs = personRepository.findTopJobInCity();

	}

	@Test
	public void averageCityAge(){
		HashMap<String, Float> rs = personRepository.averageCityAge();
		assertThat(rs).containsKeys("Havana").containsValues((float) 50.333332);
		assertThat(rs).containsKeys("Berlin").containsValues((float) 34.5);
	}

	@Test
	public void averageJobAge(){
		HashMap<String, Float> rs = personRepository.averageJobAge();
		assertThat(rs).containsKeys("Film Maker").containsValues((float) 30.8);
		assertThat(rs).containsKeys("Teacher").containsValues((float) 45.0);
	}

	@Test
	public void averageJobSalary(){
		HashMap<String, Float> rs = personRepository.averageJobSalary();
		assertThat(rs).containsKeys("Film Maker").containsValues((float) 7695.8);
		assertThat(rs).containsKeys("Teacher").containsValues((float) 8038.3335);
	}

	@Test
	public void top5HighestSalaryCities(){
		HashMap<String, Float> rs = personRepository.top5HighestSalaryCities();
	}
}
