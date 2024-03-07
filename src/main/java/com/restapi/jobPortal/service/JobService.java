package com.restapi.jobPortal.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.restapi.jobPortal.dao.JobDao;
import com.restapi.jobPortal.dto.ApprovedApplicant;
import com.restapi.jobPortal.dto.CreateJob;
import com.restapi.jobPortal.dto.Job;
import com.restapi.jobPortal.dto.JobApplication;
import com.restapi.jobPortal.dto.Location;
import com.restapi.jobPortal.dto.Qualification;
import com.restapi.jobPortal.dto.ResponseStructure;
import com.restapi.jobPortal.dto.Shift;
import com.restapi.jobPortal.dto.Skill;
import com.restapi.jobPortal.dto.WorkMode;

import redis.clients.jedis.Jedis;

@Service
public class JobService implements JobServiceInterface {

	@Autowired
	private JobDao jobDao;

	@Autowired
	private JavaMailSender javaMailSender; 

	@Value("${spring.mail.username}")
	private String fromMail;

	private Jedis jedis = new Jedis("127.0.0.1", 6379);

	@Override
	public Job createJob(CreateJob cj) {
 
		Job job = new Job();
 
		job.setCompanyId(cj.getCompanyId());
		job.setTitle(cj.getTitle());
		job.setSalary(cj.getSalary());
		job.setDescription(cj.getDescription());
		job.setBudget(cj.getBudget());
		job.setMinNoticePeriod(cj.getMinNoticePeriod());
		job.setMaxNoticePeriod(cj.getMaxNoticePeriod());
		job.setMinExperience(cj.getMinExperience());
		job.setMaxExperience(cj.getMaxExperience());
		job.setRole(cj.getRole());

		List<Qualification> qualificationList = new ArrayList<>();

		for (Integer id : cj.getQualification()) {
			Qualification qualification = new Qualification();
			qualification.setId(id);
			qualificationList.add(qualification);
		}

		job.setQualifications(qualificationList);
		// |||||||||||||||||||||||||||||||||||||||||||||||||||||||
		List<Shift> shiftList = new ArrayList<>();

		for (Integer id : cj.getShift()) {
			Shift shift = new Shift();
			shift.setId(id);
			shiftList.add(shift);
		}

		job.setShifts(shiftList);
		// |||||||||||||||||||||||||||||||||||||||||||||||||||||||

		List<Location> locationList = new ArrayList<>();

		for (Integer id : cj.getLocation()) {
			Location location = new Location();
			location.setId(id);
			locationList.add(location);
		}

		job.setLocations(locationList);
		// |||||||||||||||||||||||||||||||||||||||||||||||||||||||

		List<Skill> skillList = new ArrayList<>();

		for (Integer id : cj.getSkill()) {
			Skill skill = new Skill();
			skill.setId(id);
			skillList.add(skill);
		}

		job.setSkills(skillList);
		// |||||||||||||||||||||||||||||||||||||||||||||||||||||||

		List<WorkMode> workModeList = new ArrayList<>();

		for (Integer id : cj.getWorkMode()) {
			WorkMode workmode = new WorkMode();
			workmode.setId(id);
			workModeList.add(workmode);
		}

		job.setWorkModes(workModeList);

		Job j1 = jobDao.CreatJob(job);

		if (j1 == null) {
			return null; 
		}

		return j1; 

	}

	@Override
	public Job getJobbyId(Integer id) {

		Job job = jobDao.getJobbyId(id);

		if (job == null) {
			return null;
		}
 
		return job;
	}
    @Override
	public List<ApprovedApplicant> applyJob(Integer jobId, List<JobApplication> jobApplications)
			throws ClassNotFoundException, IOException {

		String jobKey = jobId + "";

		byte[] jobIdkeyBytes = jobKey.getBytes();
	
		if (jedis.get(jobIdkeyBytes) == null) {
	
			Job job = jobDao.getJobbyId(jobId);

			try {

				jedis.setex(jobIdkeyBytes, 3600, serialize(job));
				System.out.println(jedis.get(jobKey + "data cached successfully"));

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				jedis.close();
			}
 
		}
      
		Job job = deserialize(jedis.get(jobIdkeyBytes));
		
		ResponseStructure<List<ApprovedApplicant>> rs;
		if (job == null) {
			return null;
		}

		List<ApprovedApplicant> approvedApplicationList = new ArrayList<>();

		int numThreads = jobApplications.size();

		ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

		try {
			List<Future<JobApplication>> futures = executorService.invokeAll(jobApplications.stream()
					.map(application -> (Callable<JobApplication>) () -> processJobApplication(application, job,
							approvedApplicationList))
					.toList());

			// Wait for all threads to complete
			for (Future<JobApplication> future : futures) {
				try {
					future.get();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}

		rs = new ResponseStructure<>();
		rs.setStatusCode(HttpStatus.OK.value());
		rs.setMessage("approved applicants list");
		rs.setData(approvedApplicationList);

		UserService userService = new UserService();

		for (int i = 0; i < approvedApplicationList.size(); i++) {

			userService.sendMail(javaMailSender, fromMail, "Some Software Company",
					"Congratulations your profile has been shortlisted", approvedApplicationList.get(i).getGmail());

		}

		return approvedApplicationList;
 
	}

	public static JobApplication processJobApplication(JobApplication jobApplication, Job job,
			List<ApprovedApplicant> approvedApplicantList) {

		int count = 0;

		if (jobApplication.getExpectedSalary() <= job.getBudget()) {
			count++;
		}
		if (jobApplication.getExperience() >= job.getMinExperience()
				&& jobApplication.getExperience() <= job.getMaxExperience()) {
			count++;
		}
		if (jobApplication.getNoticePeriod() <= job.getMaxNoticePeriod() && jobApplication.getNoticePeriod() >= 0) {
			count++;
		}
		if (jobApplication.getQualification().contains(1) || jobApplication.getQualification().contains(2)
				|| jobApplication.getQualification().contains(3)) {
			count++;
		}
		if ((jobApplication.getSkill().contains(1) && jobApplication.getSkill().contains(2))
				|| jobApplication.getSkill().contains(3)) {
			count++;
		}
		if (jobApplication.getLocation().contains(1) || jobApplication.getLocation().contains(2)
				|| jobApplication.getLocation().contains(3)) {
			count++;
		}
		if (jobApplication.getShift().contains(1) || jobApplication.getShift().contains(2)
				|| jobApplication.getShift().contains(3)) {
			count++;
		}
		if (jobApplication.getWorkMode().contains(1) || jobApplication.getWorkMode().contains(2)) {
			count++;
		}

		if (count > 7) {
			ApprovedApplicant approvedApplicant = new ApprovedApplicant();
			approvedApplicant.setName(jobApplication.getName());
			approvedApplicant.setGmail(jobApplication.getEmail());
			approvedApplicant.setPhone(jobApplication.getPhone());

			approvedApplicantList.add(approvedApplicant);
			return jobApplication;
		}

		return null;

	}

	// Serialization method (using Java serialization for simplicity)
	private static byte[] serialize(Job job) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(job);
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Deserialization method (using Java serialization for simplicity)
	private static Job deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bis)) {
			return (Job) ois.readObject();
		}
	}

}
