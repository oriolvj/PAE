import React from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { ScrollArea } from "@/components/ui/scroll-area";

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

interface AssignmentPopupProps {
  isOpen: boolean;
  onClose: () => void;
  assignments: FeinaAssignadaWithCount[];
  date: Date;
}

export function AssignmentPopup({ isOpen, onClose, assignments, date }: AssignmentPopupProps) {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Assignments for {date.toLocaleDateString()}</DialogTitle>
        </DialogHeader>
        <ScrollArea className="mt-4 max-h-[60vh]">
          {assignments.map((assignment, index) => (
            <div key={index} className="mb-4 p-2 bg-gray-100 rounded">
              <h3 className="font-semibold">{assignment.nomProjecte}</h3>
              <p>Time: {assignment.startTime} - {assignment.endTime}</p>
              <p>Profile: {assignment.llocTreball}</p>
              <p>User: {assignment.username}</p>
              <p>Count: {assignment.count}</p>
            </div>
          ))}
        </ScrollArea>
      </DialogContent>
    </Dialog>
  );
}

