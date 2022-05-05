package qldt.controller

;

import qldt.AppUser;
import qldt.Student;
import qldt.UserRole;
import qldt.data.StudentRepo;
import qldt.service.AppRoleSer;
import qldt.service.AppUserSer;
import qldt.service.StudentSer;
import qldt.service.UserRoleSer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class StudentCon {

	@Autowired
	private StudentSer studentSer;
	@Autowired
	private AppUserSer appUserSer;
	@Autowired
	private AppRoleSer appRoleSer;
	@Autowired
	private UserRoleSer userRoleSer;
	@Autowired
	private StudentRepo studentRepo;

	@PostMapping("/addStudent")
	public String addStudent(@ModelAttribute Student student, @ModelAttribute AppUser appUser,
			@ModelAttribute UserRole userRole, Model model, HttpSession session) {
//        System.out.println("type added");
		// StudentSer.addStudent(Student);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
		appUser.setEnabled(true);
		appUser.setEncrytedPassword(encodedPassword);
		AppUser temp = appUserSer.addAppUser(appUser);
		student.setAppUser(temp);
		studentSer.addStudent(student);
		userRole.setAppUser(appUser);
		userRole.setAppRole(appRoleSer.findAppRole("ROLE_USER"));
		userRoleSer.addUserRole(userRole);

		model.addAttribute("newUserRole", new UserRole());
		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newStudent", new Student());
		session.setAttribute("msg", "Student Added Sucessfully...");
		return "addStudent";
	}

//    @PostMapping("AssignSubjectConform")
//    public String AssignSubjectConform(@ModelAttribute Student student, Model model){
////        System.out.println("type added");
////        StudentSer.addStudent(Student);
//        Student student1=studentRepo.findById(student.getID()).get();
//        student1.setSubjects(student.getSubjects());
//
//
//
//
//        studentSer.addStudent(student1);
//
//        List<Student> student2 =studentSer.getStudent();
//        model.addAttribute("student",  student2);
//
//        return "Studentshow";
//    }

	@GetMapping("/Student")
	public String Student(Model model) {
		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newStudent", new Student());
		model.addAttribute("newUserRole", new UserRole());
		return "addStudent";

	}

//    @GetMapping("/")
//    public String Home(Model model){
//
//        return "adminPage";
//
//    }
//
	@GetMapping("/Studentshow")
	public String STHome(Model model) {
		List<Student> student = studentSer.getStudent();
		model.addAttribute("student", student);
		return "Studentshow";
	}

	@GetMapping("/Studentshow/edit/{ID}")
	public String edit(@PathVariable("ID") long ID, Model m) {

		Student student = studentSer.getStdByID(ID);
	
//		AppUser appUser = student.getAppUser();
//	
//		model.addAttribute("appUser", appUser);
		m.addAttribute("student", student);

		return "StudentEdit";
	}
	@GetMapping("/Studentshow/edit_account/{ID}")
	public String editAccount(@PathVariable("ID") long ID, Model m) {

		AppUser appUser = studentRepo.getById(ID).getAppUser();
	
//		AppUser appUser = student.getAppUser();
//	
//		model.addAttribute("appUser", appUser);
		m.addAttribute("appUser", appUser);

		return "EditAccount";
	}
	

	@PostMapping("/Studentshow/edit/UpdateStudent")
	public String UpdateStudent(@ModelAttribute Student student,
		 Model model, HttpSession session) {

//        studentSer.addStudent(student);
//
//        model.addAttribute("newAppUser", new AppUser());
//        model.addAttribute("newStudent", new Student());
//        session.setAttribute("msg","Student Added Sucessfully...");
//        return "test";
//
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String encodedPassword = passwordEncoder.encode(student.getAppUser().getEncrytedPassword());
//		appUser.setEnabled(true);
//		appUser.setEncrytedPassword(encodedPassword);
//		appUser.setUserName(student.getAppUser().getUserName());
//		AppUser temp = appUserSer.addAppUser(appUser);
//		student.setAppUser(temp);
		studentSer.addStudent(student);

		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newStudent", new Student());
		session.setAttribute("msg", "Student Added Sucessfully...");
		return "addStudent";
	}
	@PostMapping("/Studentshow/edit_account/UpdateAccount")
	public String UpdateAccount(@ModelAttribute AppUser appUser,
		 Model model, HttpSession session) {


		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
		appUser.setEnabled(true);
		appUser.setEncrytedPassword(encodedPassword);
		appUserSer.addAppUser(appUser);
//		AppUser temp = appUserSer.addAppUser(appUser);
//		student.setAppUser(temp);
//		studentSer.addStudent(student);

		model.addAttribute("newAppUser", new AppUser());
//		model.addAttribute("newStudent", new Student());
		session.setAttribute("msg", "Student Added Sucessfully...");
		return "redirect:/Studentshow";  
	}

    @GetMapping("/Studentshow/delete/{ID}")
    public String deleteStudent(@PathVariable("ID") Long ID, HttpSession session) {
    	Student student = studentSer.getStdByID(ID);
    	AppUser appUser = student.getAppUser();
    	Long idUser = appUser.getUserId();
    	UserRole ul = userRoleSer.findAppRole(appUser);
    	
    	userRoleSer.deleteUserRoleByAppUser(ul.getId());
        studentSer.deleteByStudentId(ID);
        appUserSer.deleteByAppUserId(idUser);
        session.setAttribute("msg", "The User ID " + ID + " Deleted Succesfully");
        return "redirect:/Studentshow";
    }
//    @GetMapping("/Studentshow/delete/{ID}")
//    public String DeleteStd(@PathVariable("ID") long ID){
//
//    }
}