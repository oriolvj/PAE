import Link from 'next/link'
import { BookUserIcon, CalendarDays, CalendarPlus, CircleDollarSign, Contact2, FolderOpenDot, Home, Hourglass, Users, Variable, Workflow } from 'lucide-react'

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <div className="flex h-screen flex-col">
      <header className="flex h-16 items-center justify-between border-b px-4">
        <h1 className="text-2xl font-bold">TeamOptima</h1>
        <nav className="flex space-x-4">
          <Link href="/dashboard" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <Home className="h-5 w-5" />
            <span>Home</span>
          </Link>
          <Link href="/dashboard/planification" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <CalendarPlus className="h-5 w-5" />
            <span>Planificació Setmana</span>
          </Link>
          <Link href="/dashboard/timetable" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <CalendarDays className="h-5 w-5" />
            <span>Timetable</span>
          </Link>
          <Link href="/dashboard/project-costs" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <CircleDollarSign className="h-5 w-5" />
            <span>Costos de Projecte</span>
          </Link>
          <Link href="/dashboard/register-hours" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <Hourglass className="h-5 w-5" />
            <span>Registre d&apos;hores</span>
          </Link>
          <Link href="/dashboard/lloc-de-treball" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <Workflow className="h-5 w-5" />
            <span>Llocs de Treball</span>
          </Link>
          <Link href="/dashboard/tecnics" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <Contact2 className="h-5 w-5" />
            <span>Tècnics</span>
          </Link>
          <Link href="/dashboard/materials" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <BookUserIcon className="h-5 w-5" />
            <span>Material</span>
          </Link>
          <Link href="/dashboard/variables" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <Variable className="h-5 w-5" />
            <span>Variables</span>
          </Link>
          <Link href="/dashboard/projects" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <FolderOpenDot className="h-5 w-5" />
            <span>Projectes</span>
          </Link>
          <Link href="/dashboard/users" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <Users className="h-5 w-5" />
            <span>Users</span>
          </Link>
        </nav>
      </header>
      <main className="flex-1 overflow-auto p-4">
        {children}
      </main>
    </div>
  )
}