/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primaryNavy: '#0F172A',
        deepNavySurface: '#1E293B',
        borderSlate: '#334155',
        electricIndigo: '#3B82F6',
        royalBlue: '#2563EB',
        amberGold: '#F59E0B',
        emeraldSuccess: '#10B981',
        crimsonError: '#EF4444',
        purpleMentor: '#8B5CF6',
        cyanAccent: '#06B6D4',
      },
    },
  },
  plugins: [],
}
