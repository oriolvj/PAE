'use client';

import { useState, useEffect } from 'react';
import { format, addDays, startOfWeek, endOfWeek, isSameDay, setDay, parseISO } from 'date-fns';
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { AssignmentPopup } from '@/components/AssignmentPopup';
import React from 'react';

const HOURS = Array.from({ length: 24 }, (_, i) => i); // 0 to 23

type FeinaAssignada = {
  nomProjecte: string;
  username: string;
  id: number;
  day: string;
  startTime: string;
  endTime: string;
  llocTreball: string;
};

type FeinaAssignadaWithCount = FeinaAssignada & {
  count: number;
};

export default function TimetablePage() {
  const [currentWeek, setCurrentWeek] = useState(new Date());
  const [feinesAssignades, setFeinesAssignades] = useState<FeinaAssignadaWithCount[]>([]);
  const [projectNames, setProjectNames] = useState<string[]>([]); // Store fetched project names
  const [selectedProject, setSelectedProject] = useState<string | null>(null);
  const [selectedAssignments, setSelectedAssignments] = useState<FeinaAssignadaWithCount[]>([]);
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);

  useEffect(() => {
    fetchAssignments();
    fetchProjectNames();
  }, [currentWeek, selectedProject]);

  const fetchAssignments = async () => {
    try {
      const response = await fetch('http://10.4.41.40:8080/feinaassignada/horari');
      if (!response.ok) {
        throw new Error('Failed to fetch data');
      }
      const data: FeinaAssignada[] = await response.json();
      const compactedData = compactAssignments(data);
      setFeinesAssignades(compactedData);
    } catch (error) {
      console.error('Error fetching assignments:', error);
    }
  };

  const fetchProjectNames = async () => {
    try {
      const response = await fetch('http://10.4.41.40:8080/projectes/nomsprojectes'); // Replace with correct endpoint
      if (!response.ok) {
        throw new Error('Failed to fetch project names');
      }
      const projectData: string[] = await response.json();
      setProjectNames(projectData);
    } catch (error) {
      console.error('Error fetching project names:', error);
    }
  };

  const compactAssignments = (assignments: FeinaAssignada[]): FeinaAssignadaWithCount[] => {
    const grouped = assignments.reduce((acc, assignment) => {
      const key = `${assignment.day}-${assignment.startTime}-${assignment.endTime}-${assignment.llocTreball}-${assignment.nomProjecte}`;
      if (!acc[key]) {
        acc[key] = { ...assignment, count: 1 }; // Initialize the count
      } else {
        acc[key].count += 1; // Increment the count for matching assignments
      }
      return acc;
    }, {} as Record<string, FeinaAssignadaWithCount>);
    
    return Object.values(grouped); // Return the grouped assignments as an array
  };

  const getColorByProjectName = (projectName: string) => {
    const colors = ['#3b82f6', '#ef4444', '#10b981', '#f59e0b', '#8b5cf6'];
    return colors[projectName.length % colors.length];
  };

  const weekDays = Array.from({ length: 7 }, (_, i) => addDays(setDay(startOfWeek(currentWeek), 1), i));

  const groupAssignmentsByHour = (assignments: FeinaAssignadaWithCount[]) => {
    return assignments.reduce((acc, assignment) => {
      const startHour = parseInt(assignment.startTime.split(':')[0]);
      const endHour = parseInt(assignment.endTime.split(':')[0]);
      for (let hour = startHour; hour <= endHour; hour++) {
        if (!acc[hour]) {
          acc[hour] = [];
        }
        acc[hour].push(assignment);
      }
      return acc;
    }, {} as Record<number, FeinaAssignadaWithCount[]>);
  };

  const handleAssignmentClick = (assignments: FeinaAssignadaWithCount[], date: Date) => {
    setSelectedAssignments(assignments);
    setSelectedDate(date);
    setIsPopupOpen(true);
  };

  const filteredAssignments = selectedProject
    ? feinesAssignades.filter(feina => feina.nomProjecte === selectedProject)
    : feinesAssignades;

  return (
    <div className="space-y-4">
      <h2 className="text-2xl font-bold">Horari Setmanal</h2>
      <div className="mb-4">
        <select
          value={selectedProject || ''}
          onChange={(e) => setSelectedProject(e.target.value || null)}
          className="w-full p-2 border rounded"
        >
          <option value="">All Projects</option>
          {projectNames.map((name) => (
            <option key={name} value={name}>
              {name}
            </option>
          ))}
        </select>
      </div>
      <div className="flex justify-between items-center">
        <Button onClick={() => setCurrentWeek(addDays(currentWeek, -7))}>Setmana anterior</Button>
        <span>
          {format(startOfWeek(currentWeek, { weekStartsOn: 1 }), 'MMM d')} -{' '}
          {format(endOfWeek(currentWeek, { weekStartsOn: 1 }), 'MMM d, yyyy')}
        </span>
        <Button onClick={() => setCurrentWeek(addDays(currentWeek, 7))}>Setmana següent</Button>
      </div>
      <Card>
        <CardContent>
          <div className="grid grid-cols-8 gap-0">
            <div className="grid grid-rows-25 gap-0">
              <div className="h-16"></div>
              {HOURS.map((hour) => (
                <div
                  key={hour}
                  className="text-lg text-right pr-4 sticky left-0 bg-white z-10 h-16 flex items-center font-semibold"
                >
                  {hour.toString().padStart(2, '0')}:00
                </div>
              ))}
            </div>
            {weekDays.map((day) => {
              const dayAssignments = filteredAssignments.filter((feina) => isSameDay(parseISO(feina.day), day));
              const groupedAssignments = groupAssignmentsByHour(dayAssignments);

              return (
                <div key={day.toISOString()} className="relative">
                  <div className="h-16 font-bold text-center border-b">
                    {format(day, 'EEE')}<br />
                    {format(day, 'd')}
                  </div>
                  {HOURS.map((hour) => {
                    const hourAssignments = groupedAssignments[hour] || [];
                    return (
                      <div key={`${day.toISOString()}-${hour}`} className="relative h-16 border-t">
                        {hourAssignments.map((assignment, index) => {
                          const startHour = parseInt(assignment.startTime.split(':')[0]);
                          const endHour = parseInt(assignment.endTime.split(':')[0]);
                          const topOffset = (startHour - hour) * 4;
                          const eventHeight = (endHour - startHour + 1) * 4;

                          return (
                            <Button
                              key={`${assignment.id}-${index}`}
                              variant="outline"
                              size="sm"
                              className="absolute left-0 w-full z-20 bg-opacity-50 hover:bg-opacity-75 transition-colors"
                              onClick={() => handleAssignmentClick([assignment], day)}
                              style={{
                                top: `${topOffset}rem`,
                                height: `${eventHeight}rem`,
                                backgroundColor: getColorByProjectName(assignment.nomProjecte),
                              }}
                            >
                              {assignment.nomProjecte}
                            </Button>
                          );
                        })}
                      </div>
                    );
                  })}
                </div>
              );
            })}
          </div>
        </CardContent>
      </Card>
      {selectedDate && (
        <AssignmentPopup
          isOpen={isPopupOpen}
          onClose={() => setIsPopupOpen(false)}
          assignments={selectedAssignments}
          date={selectedDate}
        />
      )}
    </div>
  );
}