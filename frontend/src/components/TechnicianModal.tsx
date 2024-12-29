import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"

interface Technician {
  id: string
  nom: string
  horesContracte: number
  sou: number
  posicio: string
}

interface TechnicianModalProps {
  technician: Technician | null
  isOpen: boolean
  onClose: () => void
}

export function TechnicianModal({ technician, isOpen, onClose }: TechnicianModalProps) {
  if (!technician) return null

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{technician.nom}</DialogTitle>
          <DialogDescription>{technician.posicio}</DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-2 items-center gap-4">
            <span className="font-medium">Contract Hours:</span>
            <span>{technician.horesContracte} hours</span>
          </div>
          <div className="grid grid-cols-2 items-center gap-4">
            <span className="font-medium">Salary:</span>
            <span>{technician.sou} €</span>
          </div>
        </div>
        <Button onClick={onClose}>Close</Button>
      </DialogContent>
    </Dialog>
  )
}

