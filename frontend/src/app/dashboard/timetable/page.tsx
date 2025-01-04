'use client';

import { useState, useEffect } from 'react';
import { format, addDays, startOfWeek, endOfWeek, isSameDay, setDay, parseISO } from 'date-fns';
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import React from 'react';

const HOURS = Array.from({ length: 24 }, (_, i) => i); // 0 to 23

// Define FeinaAssignada type
type FeinaAssignada = {
  nomProjecte: string;
  username: string;
  id: number;
  day: string;
  startTime: string;
  endTime: string;
  technicalProfile: string;
};

// Extend FeinaAssignada to include count
type FeinaAssignadaWithCount = FeinaAssignada & {
  count: number;
};

export default function TimetablePage() {
  const [currentWeek, setCurrentWeek] = useState(new Date());
  const [feinesAssignades, setFeinesAssignades] = useState<FeinaAssignadaWithCount[]>([]);

  useEffect(() => {
    fetchRequeriments();
  }, [currentWeek]);

  const fetchRequeriments = async () => {
    try {
      const response = await fetch('http://10.4.41.40:8080/feinaassignada/horari');
      if (!response.ok) {
        throw new Error('Failed to fetch data');
      }
      const data: FeinaAssignada[] = await response.json();

      console.log(data);

      // Compact the requeriments before setting the state
      const compactedData = compactRequeriments(data);
      setFeinesAssignades(compactedData);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const compactRequeriments = (requeriments: FeinaAssignada[]): FeinaAssignadaWithCount[] => {
    const grouped = requeriments.reduce((acc, req) => {
      const key = `${req.day}-${req.startTime}-${req.endTime}-${req.technicalProfile}-${req.nomProjecte}`;
      if (!acc[key]) {
        acc[key] = { ...req, count: 1 }; // Add a count property for grouped items
      } else {
        acc[key].count += 1; // Increment the count for duplicates
      }
      return acc;
    }, {} as Record<string, FeinaAssignadaWithCount>);
    return Object.values(grouped);
  };

  const getColorByProjectName = (projectName: string) => {
    const colors = ['#3b82f6', '#ef4444', '#10b981', '#f59e0b', '#8b5cf6'];
    return colors[projectName.length % colors.length]; // Assign a color based on the string length
  };

  // Start the week on Monday
  const weekDays = Array.from({ length: 7 }, (_, i) => addDays(setDay(startOfWeek(currentWeek), 1), i));

  return (
    <div className="space-y-4">
      <h2 className="text-2xl font-bold">Horari Setmanal</h2>
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
            {/* Hours Column */}
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

            {/* Days Columns */}
            {weekDays.map((day) => (
              <div key={day.toISOString()} className="relative">
                <div className="h-16 font-bold text-center border-b">
                  {format(day, 'EEE')}<br />
                  {format(day, 'd')}
                </div>
                {/* FeinesAssignades */}
                {feinesAssignades
                  .filter((feina) => isSameDay(parseISO(feina.day), day))
                  .map((feina) => {
                    const startHour = parseInt(feina.startTime.split(':')[0]);
                    const startMinute = parseInt(feina.startTime.split(':')[1]);
                    const endHour = parseInt(feina.endTime.split(':')[0]);
                    const endMinute = parseInt(feina.endTime.split(':')[1]);

                    const topOffset = (startHour * 60 + startMinute) / 60 * 4; // 4rem per hour
                    const eventHeight = (((endHour + 1) * 60 + endMinute) - (startHour * 60 + startMinute)) / 60 * 4;

                    return (
                      <div
                        key={feina.id}
                        className="absolute left-0 m-1 w-full rounded p-2 text-sm text-white overflow-hidden"
                        style={{
                          backgroundColor: getColorByProjectName(feina.nomProjecte),
                          top: `${topOffset}rem`,
                          height: `${eventHeight}rem`,
                        }}
                        title={`${feina.nomProjecte}\n${feina.startTime} - ${feina.endTime}\n${feina.technicalProfile} - ${feina.username}\nCount: ${feina.count}`}
                      >
                        <div className="font-semibold">{feina.nomProjecte}</div>
                        <div className="text-xs">
                          {feina.startTime} - {feina.endTime}
                        </div>
                        <div className="text-xs">{feina.technicalProfile}</div>
                        <div className="text-xs">{feina.username}</div>
                        {feina.count > 1 && <div className="text-xs">x{feina.count}</div>}
                      </div>
                    );
                  })}
                {HOURS.map((hour) => (
                  <div key={`${day.toISOString()}-${hour}`} className="relative h-16 border-t"></div>
                ))}
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
