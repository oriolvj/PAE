package com.pae.pae.repositories;

import com.pae.pae.controllers.*;
import com.pae.pae.models.*;
import com.pae.pae.services.TecnicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.sql.SQLException;

import java.time.*;
import java.time.temporal.IsoFields;
import java.util.*;

@Repository
public class AlgorithmRepository {

    @Autowired
    private TecnicRepository tecnicRepository = new TecnicRepository();
    @Autowired
    private TecnicService tecnicService = new TecnicService();
    @Autowired
    private ProjecteRepository projecteRepository = new ProjecteRepository();
    @Autowired
    private RequerimentRepository requerimentRepository = new RequerimentRepository();
    @Autowired
    private FeinaAssignadaRepository feinaAssignadaRepository = new FeinaAssignadaRepository();

    private Date iniciSetmana;

    private Date fiSetmana;

    public boolean execute(Date date) throws SQLException {

        iniciSetmana = date;
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate finSemanaLocal = localDate.with(java.time.temporal.TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        fiSetmana = Date.from(finSemanaLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());



        // Create some objects as an example
        List<TecnicDTO> allEmployees = getTecnics();
        List<ProjecteDTO> projects = getProjectesDesde();

        // Automatic assignment of employees to requirements
        boolean allProjectsAssigned = automaticAssignment(projects, allEmployees);
        
        if (allProjectsAssigned) {

            System.out.println("All projects assigned: " + projects);
            return true;
        } else {
            System.out.println("There are requirements that have not been assigned");
            System.out.println("Projects: " + projects);
            return false;
        }
    }

    public boolean automaticAssignment(List<ProjecteDTO> projects, List<TecnicDTO> employees) throws SQLException {
        boolean allProjectsAssigned = true;

        List<TecnicDTO> candidates = new ArrayList<>();
        List<TecnicDTO> preferenceCandidates = new ArrayList<>();
        List<RequerimentDTO> assignedRequirements = new ArrayList<>();
        
        // Automatic assignment of employees to requirements
        for (ProjecteDTO project : projects) {
            boolean allAssigned = false;
            candidates.clear();
            preferenceCandidates.clear();

            // First, we look for available POOL employees
            candidates = findEmployeesByModality(employees, "POOL");
            //preferenceCandidates = findEmployeesByPreference(candidates, projectForm.getProjectName());
            preferenceCandidates = findEmployeesByModalityandPreference("POOL", project.getNom());
            // Check if there are employees with preference for the project
            if (!preferenceCandidates.isEmpty()) {
                allAssigned = assignEmployeesToRequirements(project, preferenceCandidates, assignedRequirements);
            } 
            // If there are still requirements without employees assigned, we look for employees in POOL
            if (!allAssigned) {
                allAssigned = assignEmployeesToRequirements(project, candidates, assignedRequirements);
            }
            // If there are still requirements without employees assigned, we look for employees in ALTAS
            if (!allAssigned) {	
                candidates = findEmployeesByModality(employees, "ALTAS");
                allAssigned = assignEmployeesToRequirements(project, candidates, assignedRequirements);
            }
            allProjectsAssigned = allProjectsAssigned &&  allAssigned;
        }
        return allProjectsAssigned;        
    }


    public boolean assignEmployeesToRequirements(ProjecteDTO project, List<TecnicDTO> candidates, List<RequerimentDTO> assignedRequirements) throws SQLException {
        Duration duration;

        List<TecnicDTO> discardedEmployees = new ArrayList<>();
        List<RequerimentDTO> requirements = getRequerimentsProjecteSetmanaNoAssignats(project.getNom());

        List<TecnicDTO> profileCandidates = new ArrayList<>();

        for (RequerimentDTO requeriment : requirements) {
            // First, we check if the requirement is not assigned
            if(!assignedRequirements.contains(requeriment)){
                // First, we look for employees with the required technical profile
                profileCandidates = findEmployeesByRol(candidates, requeriment.getTechnicalProfile());
                if (!profileCandidates.isEmpty()) {
                    duration = Duration.between(requeriment.getStartTime(), requeriment.getEndTime());
                    // We check if the employee meets the requirements of the act
                    boolean removed = false;
                    boolean assigned = false;

                    for (Iterator<TecnicDTO> iterator2 = profileCandidates.iterator(); iterator2.hasNext() && !assigned;) {
                        TecnicDTO candidate = iterator2.next();
                        // If the duration of the act is greater than 9 hours, part-time employees are discarded
                        if (duration.toMinutes() > 540 && candidate.getJornada().toString().equals("PARCIAL")) {
                            // profileCandidates.remove(candidate);
                            discardedEmployees.add(candidate);
                            removed = true;
                        }else{  // If we don't discard the employee, we check the rest of the requirements
                            // Check if the employee is available -> check if the employee is already assigned to another act at the same time
                            List<FeinaAssignadaDTO> feines = getFeinesCandidat(candidate.getUsername());
                            if (!feines.isEmpty()){
                                for (Iterator<FeinaAssignadaDTO> iterator3 = feines.iterator(); iterator3.hasNext() && !removed;) {
                                    FeinaAssignadaDTO feina = iterator3.next();

                                    // Check if the day matches
                                    if (feina.getDay().equals(requeriment.getDay())) {
                                        // Check if the time ranges overlap
                                        boolean overlap = feina.getStartTime().isBefore(requeriment.getEndTime()) &&
                                                        feina.getEndTime().isAfter(requeriment.getStartTime());

                                        if (overlap) {
                                            discardedEmployees.add(candidate);
                                            removed = true;
                                        }
                                    }
                                }
                            }
                            
                            //if (candidate.getAssignedRequirements().isEmpty() || (!removed)){
                            if (feines.isEmpty() || (!removed)){
                                // If the employee is available
                                // We check if the employee adheres to the rules of the labor contract (CONVENI)
                                if (checkLabourAgreement(candidate, requeriment, feines)){
                                    // Assign the employee to the act
                                    addFeinaAssignada(requeriment.getNomProjecte(), candidate.getUsername(), requeriment.getId());
                                    //project.getRequirements().get(project.getRequirements().indexOf(requirement)).setAssignedEmployee(candidate);
                                    assignedRequirements.add(requeriment);
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
        return assignedRequirements.size() == getRequerimentsProjecteSetmanaNoAssignats(project.getNom()).size(); // If all the requirements have been assigned, return true
    }


    public List<String> getNomProjectes(){
        return projecteRepository.getNomProjectes();
    }

    public List<ProjecteDTO> getProjectes(){
        return projecteRepository.getProjectes();
    }

    public List<ProjecteDTO> getProjectesDesde(){
        return projecteRepository.getProjectesDesdeData(iniciSetmana);
    }

    public List<RequerimentDTO> getRequerimentsProjecte(String nom){
        return requerimentRepository.getRequerimentsProjecte(nom);
    }

    public List<RequerimentDTO> getRequerimentsProjecteSetmanaNoAssignats(String nom){
        return requerimentRepository.getRequerimentsProjecteSetmanaNoAssignats(nom, iniciSetmana, fiSetmana);
    }
    public List<RequerimentDTO> getRequerimentsSetmana(String nom){
        return requerimentRepository.getRequerimentsSetmana(iniciSetmana, fiSetmana);
    }

    public List<TecnicDTO> findEmployeesByRol(List<TecnicDTO> employees, String profile) {
        return tecnicRepository.getTecnicsByLlocDeTreball(profile);
    }

    public List<TecnicDTO> findEmployeesByModality(List<TecnicDTO> employees, String modality) {
        return tecnicService.getTecnicsByModalitat(modality);
    }

    public List<TecnicDTO> findEmployeesByPreference(List<TecnicDTO> employees, String project) {
        return tecnicRepository.getTecnicsByPreferencia(project);
    }

    public List<TecnicDTO> findEmployeesByModalityandPreference(String modality, String project) {
        return tecnicService.getTecnicsByModalitatAndPreferencia(modality, project);
    }

    public List<FeinaAssignadaDTO> getFeinesCandidat(String username) {
        return feinaAssignadaRepository.getfeinesAssignadaUsuari(username);
    }



    public static boolean checkLabourAgreement(TecnicDTO candidate, RequerimentDTO requeriment, List<FeinaAssignadaDTO> feines){
        // Comprovar:
        // Superar las 9h/d máximo 3 dias consecutivos
        // Nunca superar las 50h/semana
        // Nunca superar las 40h/semana de media en un periodo de 4 semanas
        // Mínimo 48h consecutivas de descanso semanal
        // Mínimo 12h de descanso entre jornadas
        // Check:
        if (chechkMaximumDailyHours(candidate, requeriment, feines)) {  // Maximum of 12 hours per day
            if (checkMinimumRestBetweenActs(candidate, requeriment, feines)) {  // Minimum rest between acts (12 hours)
                if (checkWeeklyRest(candidate, requeriment, feines)) {  // Minimum rest of 48 hours on a week
                    if (checkConsecutiveDailyHours(candidate, requeriment, feines)) {   // Maximum of 3 consecutive days with more than 9 hours
                        if (checkMaxWeeklyHours(candidate, requeriment, feines)) {  // Maximum of 50 hours per week
                            if (checkAverageWeeklyHours(candidate, requeriment, feines)) { // Maximum of 9h/d * labor days in a month
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

    public static boolean chechkMaximumDailyHours(TecnicDTO candidate, RequerimentDTO requeriment, List<FeinaAssignadaDTO> feines){
        Boolean maximumDailyHoursNotExceeded = true;
        // Check if the employee is going to work more than 12 hours in a day
        List<FeinaAssignadaDTO> sameDayFeines = new ArrayList<>();
        for (FeinaAssignadaDTO feina : feines) {
            if (feina.getDay().equals(requeriment.getDay())) {
                sameDayFeines.add(feina);
            }
        }

        if (!sameDayFeines.isEmpty()){
            // We add the new requirement to the list to check if with it the employee is going to work more than 12 hours
            //sameDayFeines.add(requeriment);

            // Check if the employee is going to work more than 12 hours in a day
            long totalHours = 0;
            for (FeinaAssignadaDTO act : sameDayFeines) {
                totalHours += Duration.between(act.getStartTime(), act.getEndTime()).toHours();
            }
            totalHours += Duration.between(requeriment.getStartTime(), requeriment.getEndTime()).toHours();
            if (totalHours > 12) {
                maximumDailyHoursNotExceeded = false;
            }
        }// If the employee has no assigned acts in the same day, the maximum daily hours are not exceeded -> the boolean already is true

        return maximumDailyHoursNotExceeded;
    }

    public static boolean checkMinimumRestBetweenActs(TecnicDTO candidate, RequerimentDTO requeriment, List<FeinaAssignadaDTO> feines){
        Boolean minimumRest = true;
        // Check if the employee has a minimum rest of 12 hours between acts
        if (!feines.isEmpty()){
            for (FeinaAssignadaDTO assignedAct : feines) {
                // If the end time of the assigned act is less than 12 hours before the start time of the new act, the employee is discarded
                if (Duration.between(assignedAct.getStartTime(), requeriment.getStartTime()).toHours() < 12) {
                    minimumRest = false;
                }else if (Duration.between(requeriment.getEndTime(), assignedAct.getStartTime()).toHours() < 12) {
                    // If the start time of the assigned act is less than 12 hours before the end time of the new act, the employee is discarded
                    minimumRest = false;
                }
            }
        }
        return minimumRest;
    }

    public static boolean checkWeeklyRest(TecnicDTO candidate, RequerimentDTO requeriment, List<FeinaAssignadaDTO> feines){
        Boolean rest = false;
        // Check if the employee has a minimum rest of 48 hours on a week
        List<FeinaAssignadaDTO> sameWeekRequirements = new ArrayList<>();
        for (FeinaAssignadaDTO assignedAct : feines) {
            if (isSameWeek(assignedAct.getDay(), requeriment.getDay())) {
                sameWeekRequirements.add(assignedAct);
            }
        }

        if (!sameWeekRequirements.isEmpty()){
            // We add the new requirement to the list to check if with it the employee has a rest of 48 hours
            FeinaAssignadaDTO possibleFeina = new FeinaAssignadaDTO(null, candidate.getUsername(), null, requeriment.getDay(),
                                                                                requeriment.getStartTime(), requeriment.getEndTime());
            sameWeekRequirements.add(possibleFeina);
            // Sort the list by day and start time
            sameWeekRequirements.sort(Comparator.comparing(FeinaAssignadaDTO::getDay)
                    .thenComparing(FeinaAssignadaDTO::getStartTime));

            // Obtain startWeek and endWeek
            LocalDateTime weekStart = requeriment.getDay().with(DayOfWeek.MONDAY).atStartOfDay();   // Monday 00:00
            LocalDateTime weekEnd = requeriment.getDay().with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX); // Sunday 23:59

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

    public static boolean checkConsecutiveDailyHours(TecnicDTO candidate, RequerimentDTO requeriment, List<FeinaAssignadaDTO> feines){
        Boolean consecutiveHoursNotExceeded = true;
        // We only check this if the duration of the act is greater than 9 hours
        long duration = Duration.between(requeriment.getStartTime(), requeriment.getEndTime()).toMinutes();
        if (duration > 540){
            // Check if the employee has more than 9 hours in 3 consecutive days
            List<FeinaAssignadaDTO> relevantLongWorkRequirements = new ArrayList<>();
            for (FeinaAssignadaDTO assignedAct : feines){
                if (Duration.between(assignedAct.getStartTime(), assignedAct.getEndTime()).toMinutes() > 540 &&
                        (assignedAct.getDay().isAfter(requeriment.getDay().minusDays(4)) && assignedAct.getDay().isBefore(requeriment.getDay().plusDays(4)))){
                    relevantLongWorkRequirements.add(assignedAct);
                }
            }

            if (!relevantLongWorkRequirements.isEmpty()){
                // We add the new requirement to the list to check if with it the employee has more than 9 hours in 3 consecutive days
                FeinaAssignadaDTO possibleFeina = new FeinaAssignadaDTO(null, candidate.getUsername(), null, requeriment.getDay(),
                        requeriment.getStartTime(), requeriment.getEndTime());
                relevantLongWorkRequirements.add(possibleFeina);
                // Sort the list by day and start time
                relevantLongWorkRequirements.sort(Comparator.comparing(FeinaAssignadaDTO::getDay));

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

    public static boolean checkMaxWeeklyHours(TecnicDTO candidate, RequerimentDTO requeriment, List<FeinaAssignadaDTO> feines){
        boolean maxWeeklyHoursNotExceeded = true;
        // Check if the employee has worked more than 50 hours in a week
        List<FeinaAssignadaDTO> sameWeekRequirements = new ArrayList<>();
        for (FeinaAssignadaDTO assignedAct : feines) {
            if (isSameWeek(assignedAct.getDay(), requeriment.getDay())) {
                sameWeekRequirements.add(assignedAct);
            }
        }

        if (!sameWeekRequirements.isEmpty()){
            // We add the new requirement to the list to check if with it the employee has worked more than 50 hours
            FeinaAssignadaDTO possibleFeina = new FeinaAssignadaDTO(null, candidate.getUsername(), null, requeriment.getDay(),
                    requeriment.getStartTime(), requeriment.getEndTime());
            sameWeekRequirements.add(possibleFeina);

            // Check if the employee has worked more than 50 hours in a week
            long totalHours = 0;
            for (FeinaAssignadaDTO act : sameWeekRequirements) {
                totalHours += Duration.between(act.getStartTime(), act.getEndTime()).toHours();
            }
            if (totalHours > 50) {
                maxWeeklyHoursNotExceeded = false;
            }
        }// If the employee has no assigned acts in the same week, the maximum weekly hours are not exceeded -> the boolean already is true

        return maxWeeklyHoursNotExceeded;
    }

    public static boolean checkAverageWeeklyHours(TecnicDTO candidate, RequerimentDTO requeriment, List<FeinaAssignadaDTO> feines){
        boolean averageWeeklyHoursNotExceeded = true;
        // Maximum of 9h/d * labor days in a month
        // Determine the labor days in the requirement month
        int laborDaysInMonth = laborDaysInMonth(YearMonth.from(requeriment.getDay()));
        // Calculate the maximum hours that the employee can work in a month
        int maxHoursInMonth = 9 * laborDaysInMonth;
        // Calculate the hours that the employee has worked in the month
        List<FeinaAssignadaDTO> candidateRequirements = new ArrayList<>(feines);
        FeinaAssignadaDTO possibleFeina = new FeinaAssignadaDTO(null, candidate.getUsername(), null, requeriment.getDay(),
                requeriment.getStartTime(), requeriment.getEndTime());
        candidateRequirements.add(possibleFeina);
        for (FeinaAssignadaDTO assignedAct : candidateRequirements) {
            if (YearMonth.from(assignedAct.getDay()).equals(YearMonth.from(requeriment.getDay()))) {
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

    public static List<TecnicDTO> getTecnics() {
        TecnicController tecnicController = new TecnicController();
        return tecnicController.getTecnics();
    }

    /*public static List<RequerimentDTO> createRequirements(String project) {
        List<RequerimentDTO> requeriments = new ArrayList<>();

        if (project.equals("CCCB")){
            // Día 1: CCCB
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 14), LocalTime.of(10, 0), LocalTime.of(19, 0), "coordinator", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 14), LocalTime.of(10, 0), LocalTime.of(19, 30), "mixer", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 14), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Menjar amb els ulls", "Sala Teatre", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 14), LocalTime.of(10, 0), LocalTime.of(19, 30), "camera operator", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 14), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Menjar amb els ulls", "Sala Teatre", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 14), LocalTime.of(17, 0), LocalTime.of(20, 30), "support", "El renaixement", "Hall", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 14), LocalTime.of(17, 0), LocalTime.of(20, 30), "mount auxiliary", "El renaixement", "Hall", "CCCB"));

            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(19, 0), "coordinator", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(19, 30), "mixer", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Menjar amb els ulls", "Sala Teatre", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(19, 30), "camera operator", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Menjar amb els ulls", "Sala Teatre", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 15), LocalTime.of(17, 0), LocalTime.of(20, 30), "support", "El renaixement", "Hall", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 15), LocalTime.of(17, 0), LocalTime.of(20, 30), "mount auxiliary", "El renaixement", "Hall", "CCCB"));

            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 16), LocalTime.of(10, 0), LocalTime.of(19, 0), "coordinator", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 16), LocalTime.of(10, 0), LocalTime.of(19, 30), "mixer", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 16), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Menjar amb els ulls", "Sala Teatre", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 16), LocalTime.of(10, 0), LocalTime.of(19, 30), "camera operator", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 16), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Menjar amb els ulls", "Sala Teatre", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 16), LocalTime.of(17, 0), LocalTime.of(20, 30), "support", "El renaixement", "Hall", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 16), LocalTime.of(17, 0), LocalTime.of(20, 30), "mount auxiliary", "El renaixement", "Hall", "CCCB"));

            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 17), LocalTime.of(10, 0), LocalTime.of(19, 0), "coordinator", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 17), LocalTime.of(10, 0), LocalTime.of(19, 30), "mixer", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 17), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Menjar amb els ulls", "Sala Teatre", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 17), LocalTime.of(10, 0), LocalTime.of(19, 30), "camera operator", "«Tic Tac» de Rosa Vergés", "Auditori", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 17), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Menjar amb els ulls", "Sala Teatre", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 17), LocalTime.of(17, 0), LocalTime.of(20, 30), "support", "El renaixement", "Hall", "CCCB"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 17), LocalTime.of(17, 0), LocalTime.of(20, 30), "mount auxiliary", "El renaixement", "Hall", "CCCB"));
        } else if (project.equals("Parlament")){
            // Día 2: Parlament
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "coordinator", "Debat Parlamentari", "Parlament", "Parlament"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "mixer", "Debat Parlamentari", "Parlament", "Parlament"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Debat Parlamentari", "Parlament", "Parlament"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Debat Parlamentari", "Parlament", "Parlament"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "camera operator", "Debat Parlamentari", "Parlament", "Parlament"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "technical sound", "Debat Parlamentari", "Parlament", "Parlament"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 18), LocalTime.of(10, 0), LocalTime.of(14, 30), "mount auxiliary", "Debat Parlamentari", "Parlament", "Parlament"));
        } else if (project.equals("Kings League")){
            // Día 3: Kings League
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "coordinator", "Draft de Jugadores", "Kings league plant", "Kings League"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "mixer", "Draft de Jugadores", "Kings league plant", "Kings League"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "technical sound", "Draft de Jugadores", "Kings league plant", "Kings League"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "camera operator", "Draft de Jugadores", "Kings league plant", "Kings League"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "producer", "Draft de Jugadores", "Kings league plant", "Kings League"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "support", "Draft de Jugadores", "Kings league plant", "Kings League"));
            requeriments.add(new RequerimentDTO(LocalDate.of(2024, 10, 19), LocalTime.of(14, 0), LocalTime.of(22, 0), "mount auxiliary", "Draft de Jugadores", "Kings league plant", "Kings League"));
        }
        return requeriments;
    }*/


    public static void addFeinaAssignada(String nomProjecte, String username, Integer id) throws SQLException {
        FeinaAssignadaController feinaAssignadaController = new FeinaAssignadaController();
        Map<String, String> req = new HashMap<>();
        req.put("nom_projecte", nomProjecte);
        req.put("username", username);
        req.put("id", id.toString());
        feinaAssignadaController.addFeinaAssignada(req);
    }
}

