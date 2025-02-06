import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { format } from 'date-fns'

type FeinaAssignadaWithCount = {
  nomProjecte: string;
  username: string;
  id: number;
  day: string;
  startTime: string;
  endTime: string;
  llocTreball: string;
  count: number;
};

interface AssignmentPopupProps {
  isOpen: boolean;
  onClose: () => void;
  assignments: FeinaAssignadaWithCount[];
  date: Date;
  onDelete: (assignment: FeinaAssignadaWithCount) => void;
}

export function AssignmentPopup({ isOpen, onClose, assignments, date, onDelete }: AssignmentPopupProps) {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Assignments for {format(date, 'MMMM d, yyyy')}</DialogTitle>
        </DialogHeader>
        <div className="space-y-4">
          {assignments.map((assignment, index) => (
            <div key={`${assignment.id}-${index}`} className="border p-4 rounded-md">
              <h3 className="font-bold">{assignment.nomProjecte}</h3>
              <p>Time: {assignment.startTime} - {assignment.endTime}</p>
              <p>Location: {assignment.llocTreball}</p>
              <p>Username: {assignment.username}</p>
              <p>Occurrences: {assignment.count}</p>
              <Button 
                variant="destructive" 
                size="sm" 
                className="mt-2"
                onClick={() => onDelete(assignment)}
              >
                Delete
              </Button>
            </div>
          ))}
        </div>
      </DialogContent>
    </Dialog>
  )
}
