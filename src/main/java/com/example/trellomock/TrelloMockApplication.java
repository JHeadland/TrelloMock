package com.example.trellomock;

import com.example.trellomock.member.Member;
import com.example.trellomock.member.MemberRepository;
import com.example.trellomock.task.Task;
import com.example.trellomock.task.TaskRepository;
import com.example.trellomock.taskCategory.TaskCategory;
import com.example.trellomock.taskCategory.TaskCategoryRepository;
import com.example.trellomock.team.Team;
import com.example.trellomock.team.TeamRepository;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.example.trellomock.DateOps;

import java.time.Duration;

@EnableScheduling
@SpringBootApplication
public class TrelloMockApplication {

	private static final Logger log = LoggerFactory.getLogger(TrelloMockApplication.class);

	@Autowired
	TeamRepository teamRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	TaskRepository taskRepository;
	@Autowired
	TaskCategoryRepository taskCategoryRepository;

	public static void main(String[] args) {
		Application.launch(JavaFXApp.class, args);
	}

	@Bean
	public CommandLineRunner demo(TeamRepository teamRepository, MemberRepository memberRepository, TaskRepository taskRepository, TaskCategoryRepository taskCategoryRepository) {

		return (args) -> {

			// Creating a new Team
			Team team = new Team("team1",1L);
			teamRepository.save(team);
			teamRepository.save(new Team("Akatsuki", 2L));
			// save a few Users
			memberRepository.save(new Member("Ad", "Min", "admin", "password", team));
			memberRepository.save(new Member("John", "Headland", "johntheadland@gmail.com", "password", team));
			memberRepository.save(new Member("Nathan", "Nava", "ncolenava@gmail.com","password", team));
			memberRepository.save(new Member("Niels", "Moeller", "nielsmoeller9@gmail.com", "password", team));
			memberRepository.save(new Member("Miguel", "Dominguez", "migueld225@gmail.com", "password", team));
			memberRepository.save(new Member("Itachi", "Uchiha", "youngsharingan@hidden.leaf", "password", teamRepository.findById(2L)));

			// set Ad Min to admin
			Member mem = memberRepository.findById(1L);
			mem.setAdminPrivileges(true);
			memberRepository.save(mem);

			// Create new Task Category
			TaskCategory category1 = new TaskCategory(1L,"Front end development", "Ad Min", java.time.LocalDate.now().toString());
			TaskCategory category2 = new TaskCategory(2L,"Back end development", "Ad Min", java.time.LocalDate.now().toString());
			category1.setCategoryDescription("Tasks related to front end GUI development.");
			category2.setCategoryDescription("Tasks related to back end database development.");
			taskCategoryRepository.save(category1);
			taskCategoryRepository.save(category2);


			//Create Some Tasks
			taskRepository.save(new Task(1L, 0, "This is an example of a task",category1,"Admin"));
			taskRepository.save(new Task(2L, 0, "This is another example of a task",category1,"Admin"));
			taskRepository.save(new Task(3L, 1, "This is a third example of a task",category1,"Admin"));
			taskRepository.save(new Task(4L, 1, "This is a fourth example of a task",category1,"Admin"));
			taskRepository.save(new Task(5L, 2, "This is a fifth example of a task",category1,"Admin"));
			taskRepository.save(new Task(6L, 2, "This is a sixth example of a task",category2,"Admin"));
			taskRepository.save(new Task(7L, 3, "This is a seventh example of a task",category2,"Admin"));
			taskRepository.save(new Task(8L, 3, "This is a eighth example of a task",category2,"Admin"));
			taskRepository.save(new Task(9L, 4, "This is a ninth example of a task",category2,"Admin"));
			taskRepository.save(new Task(10L, 4, "This is a tenth example of a task",category2,"Admin"));
			taskRepository.save(new Task(11L, 4, "This is an eleventh example of a task",category2,"Admin"));

			Task temp = taskRepository.findById(10L);
			temp.setPriority(1);
			taskRepository.save(temp);
			// fetch all Users
			log.info("Users found with findAll():");
			log.info("-------------------------------");
			for (Member Member : memberRepository.findAll()) {
				log.info(Member.toString());
			}
			log.info("");

			// fetch an individual User by ID
			Member User = memberRepository.findById(1L);
			log.info("User found with findById(1L):");
			log.info("--------------------------------");
			log.info(User.toString());
			log.info("");

			// fetch Users by last name
			log.info("User found with findByLastName('Headland'):");
			log.info("--------------------------------------------");
			memberRepository.findByLastName("Headland").forEach(headland -> {
				log.info(headland.toString());
			});
			log.info("");


			// fetch Users by findByTeam
			log.info("User found with findByTeam('team'):");
			log.info("--------------------------------------------");
			memberRepository.findByTeam(team).forEach(teamMember -> {
				log.info(teamMember.toString());
			});
			log.info("");


			// fetch Users by findByteamName
			log.info("User found with findByteamName ");
			log.info("--------------------------------------------");
			log.info(String.valueOf(teamRepository.findByteamName("team1").getMembers()));
			log.info("");


			// find by id
			Task task = taskRepository.findById(1L);
			log.info("Task found with findById(1L):");
			log.info("--------------------------------");
			log.info(task.toString());
			log.info("");

			log.info("Task found with findById(3L):");
			log.info("--------------------------------");
			log.info(taskRepository.findById(3L).toString());
			log.info("");

			// Assign task to user and display
			Member memberWithTask = memberRepository.findById(1L);
			memberWithTask.assignTask(taskRepository.findById(2L).GetTaskID());
			memberWithTask.assignTask(taskRepository.findById(3L).GetTaskID());
			memberWithTask.assignTask(taskRepository.findById(4L).GetTaskID());
			memberWithTask.assignTask(taskRepository.findById(5L).GetTaskID());
			memberWithTask.assignTask(taskRepository.findById(6L).GetTaskID());
			memberWithTask.assignTask(taskRepository.findById(7L).GetTaskID());
			memberWithTask.assignTask(taskRepository.findById(8L).GetTaskID());
			memberWithTask.assignTask(taskRepository.findById(9L).GetTaskID());
			memberWithTask.assignTask(taskRepository.findById(10L).GetTaskID());
			memberRepository.save(memberWithTask);
			Member memberWithTask2 = memberRepository.findById(2L);
			memberWithTask2.assignTask(taskRepository.findById(11L).GetTaskID());
			memberRepository.save(memberWithTask2);

			log.info("Assigned task ID found");
			log.info("--------------------------------------------");
			log.info(String.valueOf(memberRepository.findById(1L).getAssignedTasks()));
			log.info("");



			// fetch Tasks by findByTaskCategory
			log.info("Tasks found with find By TaskCategory('taskCategory'):");
			log.info("--------------------------------------------");
			taskRepository.findByTaskCategory(category1).forEach(taskCategory -> {
				log.info(taskCategory.toString());
			});
			log.info("");


			log.info("deleting tasks with ID: 1L");
			// delete by id
			taskRepository.deleteById(1L);

			// fetch all Tasks
			log.info("Updated list of tasks:");
			log.info("-------------------------------");
			for (Task t : taskRepository.findAll()) {
				log.info(t.toString());
			}
			log.info("");


			//fetch a task for modification
			Task t = taskRepository.findById(3L);
			log.info("Task found with findById(3L):");
			log.info("--------------------------------");
			log.info(t.toString());
			log.info("");

			t.SetColor(Color.WHITE.toString());
			t.SetDescription(("This task has been modified!"));
			taskRepository.save(t);

			t = taskRepository.findById((3L));
			log.info("Modified task:");
			log.info(t.toString());


		};
	}

}
