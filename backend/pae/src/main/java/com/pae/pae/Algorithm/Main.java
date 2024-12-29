package com.pae.pae.Algorithm;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;

import com.pae.pae.Algorithm.Classes.Employee;
import com.pae.pae.Algorithm.Classes.Project;
import com.pae.pae.Algorithm.Classes.Requirement;

public class Main {
    public static void main(String[] args) {

        // Create some objects as an example
        List<Employee> employees = createEmployees();
        List<Requirement> requirementsCCCB = createRequirements("CCCB");
        List<Requirement> requirementsParlament = createRequirements("Parlament");
        List<Requirement> requirementsKingsLeague = createRequirements("Kings League");

        // I recieve the list of projects to planify but with the requirements not assigned
        List<Project> projects = new ArrayList<>();
        projects.add(new Project("CCCB", LocalDate.of(2024, 10, 14), LocalDate.of(2024, 10, 17), requirementsCCCB));
        projects.add(new Project("Parlament", LocalDate.of(2024, 10, 18), LocalDate.of(2024, 10, 18), requirementsParlament));
        projects.add(new Project("Kings League", LocalDate.of(2024, 10, 19), LocalDate.of(2024, 10, 19), requirementsKingsLeague));

        // Automatic assignment of employees to requirements
        boolean allProjectsAssigned = automaticAssignment(projects, employees);
        
        if (allProjectsAssigned) {
            System.out.println("All projects assigned: " + projects);
        } else {
            System.out.println("There are requirements that have not been assigned");
            System.out.println("Projects: " + projects);
        }
    }

    public static boolean automaticAssignment(List<Project> projects, List<Employee> employees) {
        boolean allProjectsAssigned = false;

        List<Employee> candidates = new ArrayList<>();
        List<Employee> preferenceCandidates = new ArrayList<>();
        
        // Automatic assignment of employees to requirements
        for (Project project : projects) {
            boolean allAssigned = false;
            candidates.clear();
            preferenceCandidates.clear();

            // First, we look for available POOL employees
            candidates = findEmployeesByModality(employees, "POOL");
            preferenceCandidates = findEmployeesByPreference(candidates, project.getProjectName());
            // Check if there are employees with preference for the project
            if (!preferenceCandidates.isEmpty()) {
                allAssigned = assignEmployeesToRequirements(project, preferenceCandidates);
            } else {
                allAssigned = assignEmployeesToRequirements(project, candidates);
            }
            // If there are still requirements without employees assigned, we look for employees in POOL
            if (!allAssigned) {
                allAssigned = assignEmployeesToRequirements(project, candidates);
            }
            // If there are still requirements without employees assigned, we look for employees in ALTAS
            if (!allAssigned) {	
                candidates = findEmployeesByModality(employees, "ALTAS");
                allAssigned = assignEmployeesToRequirements(project, candidates);
            }
            allProjectsAssigned = allProjectsAssigned &&  allAssigned;
        }
        return allProjectsAssigned;        
    }

    public static boolean assignEmployeesToRequirements(Project project, List<Employee> candidates) {
        Duration duration;

        List<Employee> discardedEmployees = new ArrayList<>();
        List<Requirement> requirements = project.getRequirements();
        List<Requirement> assignedRequirements = new ArrayList<>();

        List<Employee> profileCandidates = new ArrayList<>();

        for (Requirement requirement : requirements) {
            if(requirement.getAssignedEmployee() != null){
                assignedRequirements.add(requirement);
            }else{
                // First, we look for employees with the required technical profile
                profileCandidates = findEmployeesByProfile(candidates, requirement.getTechnicalProfile());
                if (!profileCandidates.isEmpty()) {
                    duration = Duration.between(requirement.getStartTime(), requirement.getEndTime());
                    // We check if the employee meets the requirements of the act
                    boolean removed = false;
                    boolean assigned = false;

                    for (Iterator<Employee> iterator2 = profileCandidates.iterator(); iterator2.hasNext() && !assigned;) {
                        Employee candidate = iterator2.next();
                        // If the duration of the act is greater than 9 hours, part-time employees are discarded
                        if (duration.toMinutes() > 540 && candidate.getModality().equals("PART_TIME")) {
                            // profileCandidates.remove(candidate);
                            discardedEmployees.add(candidate);
                            removed = true;
                        }else{  // If we don't discard the employee, we check the rest of the requirements
                            // Check if the employee is available -> check if the employee is already assigned to another act at the same time
                            if (!candidate.getAssignedRequirements().isEmpty()){
                                for (Iterator<Requirement> iterator3 = candidate.getAssignedRequirements().iterator(); iterator3.hasNext() && !removed;) {
                                    Requirement assignedAct = iterator3.next();
                                    // If the employee is already assigned to another act the same day at the same time, is discarded
                                    if (assignedAct.getDay().equals(requirement.getDay()) && assignedAct.getStartTime().equals(requirement.getStartTime())) {
                                        // profileCandidates.remove(candidate);
                                        discardedEmployees.add(candidate);
                                        removed = true;
                                    }
                                }
                            }
                            
                            if (candidate.getAssignedRequirements().isEmpty() || (!removed)){
                                // If the employee is available
                                // We check if the employee adheres to the rules of the labor contract (CONVENI)
                                if (checkLabourAgreement(candidate, requirement)){
                                    // Assign the employee to the act
                                    requirement.setAssignedEmployee(candidate);
                                    candidate.assignRequirement(requirement);
                                    //project.getRequirements().get(project.getRequirements().indexOf(requirement)).setAssignedEmployee(candidate);
                                    assignedRequirements.add(requirement);
                                    assigned = true;
                                } else {
                                    // If the employee does not meet the requirements of the act, is discarded
                                    discardedEmployees.add(candidate);
                                } 
                            }
                        }                  
                    }
                }
            }
        }
        return assignedRequirements.size() == project.getRequirements().size(); // If all the requirements have been assigned, return true
    }

    public static List<ProjectForm> createProjectForms(List<Requirement> requirementsCCCB, List<Requirement> requirementsParlament, List<Requirement> requirementsKingsLeague) {
        List<ProjectForm> projectForms = new ArrayList<>();
        projectForms.add(new ProjectForm("CCCB", LocalDate.of(2024, 10, 14), LocalDate.of(2024, 10, 17), requirementsCCCB));
        projectForms.add(new ProjectForm("Parlament", LocalDate.of(2024, 10, 18), LocalDate.of(2024, 10, 18), requirementsParlament));
        projectForms.add(new ProjectForm("Kings League", LocalDate.of(2024, 10, 19), LocalDate.of(2024, 10, 19), requirementsKingsLeague));
        return projectForms;
    }

    public static List<Employee> findEmployeesByProfile(List<Employee> employees, String profile) {
        List<Employee> candidates = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getTechnicalProfile().equals(profile)) {
                candidates.add(employee);
            }
        }
        return candidates;
    }

    public static List<Employee> findEmployeesByModality(List<Employee> employees, String modality) {
        List<Employee> candidates = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getModality().equals(modality)) {
                candidates.add(employee);
            }
        }
        return candidates;
    }

    public static List<Employee> findEmployeesByPreference(List<Employee> employees, String project) {
        List<Employee> candidates = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getPreferenceActs() != null && employee.getPreferenceActs().contains(project)) {
                candidates.add(employee);
            }
        }
        return candidates;
    }

    public static boolean checkLabourAgreement(Employee candidate, Requirement requirement){
        // Comprovar:
                    // Superar las 9h/d máximo 3 dias consecutivos
                    // Nunca superar las 50h/semana
                    // Nunca superar las 40h/semana de media en un periodo de 4 semanas
                    // Mínimo 48h consecutivas de descanso semanal
                    // Mínimo 12h de descanso entre jornadas
        // Check:
        if (chechkMaximumDailyHours(candidate, requirement)) {  // Maximum of 12 hours per day
            if (checkMinimumRestBetweenActs(candidate, requirement)) {  // Minimum rest between acts (12 hours)
                if (checkWeeklyRest(candidate, requirement)) {  // Minimum rest of 48 hours on a week
                    if (checkConsecutiveDailyHours(candidate, requirement)) {   // Maximum of 3 consecutive days with more than 9 hours
                        if (checkMaxWeeklyHours(candidate, requirement)) {  // Maximum of 50 hours per week
                            if (checkAverageWeeklyHours(candidate, requirement)) { // Maximum of 9h/d * labor days in a month
                                // The employee meets all the rules of the agreement
                                return true;
                            }
                        }
                    }
                }
            }
        }
        // If any check fails, the employee does not comply with the agreement
        return false;
    }

    public static boolean chechkMaximumDailyHours(Employee candidate, Requirement requirement){
        Boolean maximumDailyHoursNotExceeded = true;
        // Check if the employee is going to work more than 12 hours in a day
        List<Requirement> sameDayRequirements = new ArrayList<>();
        for (Requirement assignedAct : candidate.getAssignedRequirements()) {
            if (assignedAct.getDay().equals(requirement.getDay())) {
                sameDayRequirements.add(assignedAct);
            }
        }

        if (!sameDayRequirements.isEmpty()){
            // We add the new requirement to the list to check if with it the employee is going to work more than 12 hours
            sameDayRequirements.add(requirement);
            
            // Check if the employee is going to work more than 12 hours in a day
            long totalHours = 0;
            for (Requirement act : sameDayRequirements) {
                totalHours += Duration.between(act.getStartTime(), act.getEndTime()).toHours();
            }
            if (totalHours > 12) {
                maximumDailyHoursNotExceeded = false;
            }
        }// If the employee has no assigned acts in the same day, the maximum daily hours are not exceeded -> the boolean already is true

        return maximumDailyHoursNotExceeded;
    }

    public static boolean checkMinimumRestBetweenActs(Employee candidate, Requirement requirement){
        Boolean minimumRest = true;
        // Check if the employee has a minimum rest of 12 hours between acts
        if (!candidate.getAssignedRequirements().isEmpty()){
            for (Requirement assignedAct : candidate.getAssignedRequirements()) {
                // If the end time of the assigned act is less than 12 hours before the start time of the new act, the employee is discarded
                if (Duration.between(assignedAct.getEndTime(), requirement.getStartTime()).toHours() < 12) {
                    minimumRest = false;
                }else if (Duration.between(requirement.getEndTime(), assignedAct.getStartTime()).toHours() < 12) {
                    // If the start time of the assigned act is less than 12 hours before the end time of the new act, the employee is discarded
                    minimumRest = false;
                }
            }
        }
        return minimumRest;
    }

    public static boolean checkWeeklyRest(Employee candidate, Requirement requirement){
        Boolean rest = false;
        // Check if the employee has a minimum rest of 48 hours on a week
        List<Requirement> sameWeekRequirements = new ArrayList<>();
        for (Requirement assignedAct : candidate.getAssignedRequirements()) {
            if (isSameWeek(assignedAct.getDay(), requirement.getDay())) {
                sameWeekRequirements.add(assignedAct);
            }
        }

        if (!sameWeekRequirements.isEmpty()){
            // We add the new requirement to the list to check if with it the employee has a rest of 48 hours
            sameWeekRequirements.add(requirement);
            // Sort the list by day and start time
            sameWeekRequirements.sort(Comparator.comparing(Requirement::getDay)
                                .thenComparing(Requirement::getStartTime));
            
            // Obtain startWeek and endWeek
            LocalDateTime weekStart = requirement.getDay().with(DayOfWeek.MONDAY).atStartOfDay();   // Monday 00:00
            LocalDateTime weekEnd = requirement.getDay().with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX); // Sunday 23:59

            // Check 3 different cases:
            // 1. If the rest between the start of the week and the first act is more or equal than 48 hours
            LocalDateTime firstAct = LocalDateTime.of(sameWeekRequirements.get(0).getDay(),sameWeekRequirements.get(0).getStartTime());
            if (Duration.between(weekStart, firstAct).toHours() >= 48) {
                rest = true;
            }else{
                // 2. If the rest between the last act and the end of the week is more or equal than 48 hours
                LocalDateTime lastAct = LocalDateTime.of(sameWeekRequirements.get(sameWeekRequirements.size()-1).getDay(),sameWeekRequirements.get(sameWeekRequirements.size()-1).getEndTime());
                if (Duration.between(lastAct, weekEnd).toHours() >= 48) {
                    rest = true;
                }else{
                    // 3. If the rest between two consecutive acts is more or equal than 48 hours
                    for (int i = 0; i < sameWeekRequirements.size()-1; i++) {   // go across the list
                        LocalDateTime act1 = LocalDateTime.of(sameWeekRequirements.get(i).getDay(),sameWeekRequirements.get(i).getEndTime());
                        LocalDateTime act2 = LocalDateTime.of(sameWeekRequirements.get(i+1).getDay(),sameWeekRequirements.get(i+1).getStartTime());
                        if (Duration.between(act1, act2).toHours() >= 48) {
                            rest = true;
                        }else{
                            // If in any of the cases the rest >= 48 hours, the rest with this new requirement is false so, we can't assign to this employee
                            rest = false;
                        }
                    }
                }
            }
        }else{
            // If the employee has no assigned acts in the same week, the rest is true
            rest = true;
        }
        return rest;
    }

    public static boolean checkConsecutiveDailyHours(Employee candidate, Requirement requirement){
        Boolean consecutiveHoursNotExceeded = true;
        // We only check this if the duration of the act is greater than 9 hours
        long duration = Duration.between(requirement.getStartTime(), requirement.getEndTime()).toMinutes();
        if (duration > 540){
            // Check if the employee has more than 9 hours in 3 consecutive days
            List<Requirement> relevantLongWorkRequirements = new ArrayList<>();
            for (Requirement assignedAct : candidate.getAssignedRequirements()){
                if (Duration.between(assignedAct.getStartTime(), assignedAct.getEndTime()).toMinutes() > 540 &&
                (assignedAct.getDay().isAfter(requirement.getDay().minusDays(4)) && assignedAct.getDay().isBefore(requirement.getDay().plusDays(4)))){
                    relevantLongWorkRequirements.add(assignedAct);
                }
            }

            if (!relevantLongWorkRequirements.isEmpty()){
                // We add the new requirement to the list to check if with it the employee has more than 9 hours in 3 consecutive days
                relevantLongWorkRequirements.add(requirement);
                // Sort the list by day and start time
                relevantLongWorkRequirements.sort(Comparator.comparing(Requirement::getDay));
                                            
                // Check if the employee has worked more than 9 hours in 3 consecutive days
                int consecutiveDays = 1;
                for (int i = 1; i < relevantLongWorkRequirements.size(); i++){
                    // Verify if the acts are consecutive days
                    if (relevantLongWorkRequirements.get(i).getDay().minusDays(1).equals(relevantLongWorkRequirements.get(i-1).getDay())){
                        consecutiveDays++;
                        if (consecutiveDays > 3){
                            // If the employee has worked more than 9 hours in 3 consecutive days, the consecutive hours are exceeded
                            consecutiveHoursNotExceeded = false;
                        }
                    }else{
                        consecutiveDays = 1;
                    }
                }

            }else{
                // If the employee has no assigned acts in the same week, the consecutive hours are not exceeded
                consecutiveHoursNotExceeded = true;
            }
        }else{
            // If the duration of the act is less than 9 hours, the consecutive hours are not exceeded
            consecutiveHoursNotExceeded = true;
        }
        return consecutiveHoursNotExceeded;
    }

    public static boolean checkMaxWeeklyHours(Employee candidate, Requirement requirement){
        boolean maxWeeklyHoursNotExceeded = true;
        // Check if the employee has worked more than 50 hours in a week
        List<Requirement> sameWeekRequirements = new ArrayList<>();
        for (Requirement assignedAct : candidate.getAssignedRequirements()) {
            if (isSameWeek(assignedAct.getDay(), requirement.getDay())) {
                sameWeekRequirements.add(assignedAct);
            }
        }

        if (!sameWeekRequirements.isEmpty()){
            // We add the new requirement to the list to check if with it the employee has worked more than 50 hours
            sameWeekRequirements.add(requirement);
                    
            // Check if the employee has worked more than 50 hours in a week
            long totalHours = 0;
            for (Requirement act : sameWeekRequirements) {
                totalHours += Duration.between(act.getStartTime(), act.getEndTime()).toHours();
            }
            if (totalHours > 50) {
                maxWeeklyHoursNotExceeded = false;
            }
        }// If the employee has no assigned acts in the same week, the maximum weekly hours are not exceeded -> the boolean already is true

        return maxWeeklyHoursNotExceeded;
    }

    public static boolean checkAverageWeeklyHours(Employee candidate, Requirement requirement){
        boolean averageWeeklyHoursNotExceeded = true;
        // Maximum of 9h/d * labor days in a month
        // Determine the labor days in the requirement month
        int laborDaysInMonth = laborDaysInMonth(YearMonth.from(requirement.getDay()));
        // Calculate the maximum hours that the employee can work in a month
        int maxHoursInMonth = 9 * laborDaysInMonth;
        // Calculate the hours that the employee has worked in the month
        List<Requirement> candidateRequirements = new ArrayList<>(candidate.getAssignedRequirements());
        candidateRequirements.add(requirement);
        for (Requirement assignedAct : candidateRequirements) {
            if (YearMonth.from(assignedAct.getDay()).equals(YearMonth.from(requirement.getDay()))) {
                maxHoursInMonth -= Duration.between(assignedAct.getStartTime(), assignedAct.getEndTime()).toHours();
            }
        }
        if (maxHoursInMonth < 0) {
            averageWeeklyHoursNotExceeded = false;
        }

        return averageWeeklyHoursNotExceeded;
    }

    // Method to check if two dates are in the same week
    public static boolean isSameWeek(LocalDate date1, LocalDate date2) {
        // Compare week ISO and year
        return date1.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == date2.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            && date1.getYear() == date2.getYear();
    }

    private static int laborDaysInMonth(YearMonth month) {
        int workingDays = 0;

        // Iterate over the days of the month
        for (int day = 1; day <= month.lengthOfMonth(); day++) {
            LocalDate date = month.atDay(day);
            // Labor Days -> from Monday to Friday
            if (!date.getDayOfWeek().name().equals("SATURDAY") && !date.getDayOfWeek().name().equals("SUNDAY")) {
                workingDays++;
            }
        }

        return workingDays;
    }

    public static List<Employee> createEmployees() {
        List<Employee> employees = new ArrayList<>();
        // Create an employee
        employees.add(new Employee("Marta Ferrer", "coordinator", "POOL", "FULL_TIME", new ArrayList<>(Arrays.asList("Parlament")), new ArrayList<Requirement>()));
        employees.add(new Employee("Pau Riera", "mixer", "ALTAS", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Ana Gómez", "technical sound", "POOL", "PART_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Jordi Puig", "camera operator", "POOL", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Clara Benet", "camera operator", "ALTA", "PART_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Carlos Martínez", "producer", "POOL", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Laia Costa", "support", "POOL", "PART_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Ismael Morales", "mount auxiliary", "ALTAS", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Sílvia Soler", "coordinator", "POOL", "FULL_TIME", new ArrayList<>(Arrays.asList("CCCB")), new ArrayList<Requirement>()));
        employees.add(new Employee("Jaume Rovira", "technical sound", "POOL", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Lucía Ibáñez", "producer", "POOL", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Marc Serra", "mixer", "ALTAS", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Alba Rodríguez", "support", "POOL", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Óscar Aguilar", "camera operator", "POOL", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Núria Bosch", "mount auxiliary", "ALTAS", "PART_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Daniel Vidal", "coordinator", "POOL", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));
        employees.add(new Employee("Adrià Pons", "mount auxiliary", "POOL", "FULL_TIME", new ArrayList<String>(), new ArrayList<Requirement>()));

        return employees;
    }

    public static List<Requirement> createRequirements(String project) {
        List<Requirement> requirements = new ArrayList<>();

        if (project.equals("CCCB")){
            // Día 1: CCCB
            requirements.add(new Requirement(LocalDate.of(2024, 10, 14), LocalTime.of(10, 0), LocalTime.of(19, 0), "coordinator", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 14), LocalTime.of(10, 0), LocalTime.of(19, 30), "mixer", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 14), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Menjar amb els ulls", "Sala Teatre", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 14), LocalTime.of(10, 0), LocalTime.of(19, 30), "camera operator", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 14), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Menjar amb els ulls", "Sala Teatre", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 14), LocalTime.of(17, 0), LocalTime.of(20, 30), "support", "El renaixement", "Hall", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 14), LocalTime.of(17, 0), LocalTime.of(20, 30), "mount auxiliary", "El renaixement", "Hall", null));

            requirements.add(new Requirement(LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(19, 0), "coordinator", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(19, 30), "mixer", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Menjar amb els ulls", "Sala Teatre", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(19, 30), "camera operator", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Menjar amb els ulls", "Sala Teatre", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 15), LocalTime.of(17, 0), LocalTime.of(20, 30), "support", "El renaixement", "Hall", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 15), LocalTime.of(17, 0), LocalTime.of(20, 30), "mount auxiliary", "El renaixement", "Hall", null));

            requirements.add(new Requirement(LocalDate.of(2024, 10, 16), LocalTime.of(10, 0), LocalTime.of(19, 0), "coordinator", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 16), LocalTime.of(10, 0), LocalTime.of(19, 30), "mixer", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 16), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Menjar amb els ulls", "Sala Teatre", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 16), LocalTime.of(10, 0), LocalTime.of(19, 30), "camera operator", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 16), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Menjar amb els ulls", "Sala Teatre", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 16), LocalTime.of(17, 0), LocalTime.of(20, 30), "support", "El renaixement", "Hall", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 16), LocalTime.of(17, 0), LocalTime.of(20, 30), "mount auxiliary", "El renaixement", "Hall", null));

            requirements.add(new Requirement(LocalDate.of(2024, 10, 17), LocalTime.of(10, 0), LocalTime.of(19, 0), "coordinator", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 17), LocalTime.of(10, 0), LocalTime.of(19, 30), "mixer", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 17), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Menjar amb els ulls", "Sala Teatre", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 17), LocalTime.of(10, 0), LocalTime.of(19, 30), "camera operator", "«Tic Tac» de Rosa Vergés", "Auditori", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 17), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Menjar amb els ulls", "Sala Teatre", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 17), LocalTime.of(17, 0), LocalTime.of(20, 30), "support", "El renaixement", "Hall", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 17), LocalTime.of(17, 0), LocalTime.of(20, 30), "mount auxiliary", "El renaixement", "Hall", null));
        } else if (project.equals("Parlament")){
            // Día 2: Parlament
            requirements.add(new Requirement(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "coordinator", "Debat Parlamentari", "Parlament", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "mixer", "Debat Parlamentari", "Parlament", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Debat Parlamentari", "Parlament", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Debat Parlamentari", "Parlament", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Debat Parlamentari", "Parlament", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Debat Parlamentari", "Parlament", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "mount auxiliary", "Debat Parlamentari", "Parlament", null));
        } else if (project.equals("Kings League")){
            // Día 3: Kings League
            requirements.add(new Requirement(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "coordinator", "Draft de Jugadores", "Kings league plant", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "mixer", "Draft de Jugadores", "Kings league plant", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "technical sound", "Draft de Jugadores", "Kings league plant", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "camera operator", "Draft de Jugadores", "Kings league plant", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "producer", "Draft de Jugadores", "Kings league plant", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "support", "Draft de Jugadores", "Kings league plant", null));
            requirements.add(new Requirement(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "mount auxiliary", "Draft de Jugadores", "Kings league plant", null));
        }
        return requirements;
    }
}