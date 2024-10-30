import Link from 'next/link'
import { Calendar, Home, Users } from 'lucide-react'

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <div className="flex h-screen flex-col">
      <header className="flex h-16 items-center justify-between border-b px-4">
        <h1 className="text-2xl font-bold">Dashboard</h1>
        <nav className="flex space-x-4">
          <Link href="/dashboard" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <Home className="h-5 w-5" />
            <span>Home</span>
          </Link>
          <Link href="/dashboard/timetable" className="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
            <Calendar className="h-5 w-5" />
            <span>Timetable</span>
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