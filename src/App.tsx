import React, { useState } from 'react';
import { 
  Smartphone, Code2, Download, Github, Sparkles, 
  ExternalLink, Terminal, ShieldCheck, CheckCircle2, 
  BookOpen, Star, HelpCircle, Layers, Play
} from 'lucide-react';
import { PhoneSimulator } from './components/PhoneSimulator';
import { CodeReader } from './components/CodeReader';
import { GitHubDownloadModal } from './components/GitHubDownloadModal';

export default function App() {
  const [viewMode, setViewMode] = useState<'preview' | 'code' | 'split'>('preview');
  const [isDownloadModalOpen, setIsDownloadModalOpen] = useState(false);

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col selection:bg-blue-600 selection:text-white">
      {/* Top Navbar */}
      <header className="h-16 border-b border-slate-800 bg-slate-900/90 backdrop-blur-md px-4 sm:px-6 flex items-center justify-between z-30 sticky top-0">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-blue-600 via-indigo-500 to-amber-500 flex items-center justify-center text-white shadow-lg shadow-blue-500/20">
            <span className="font-black text-xl tracking-tighter">AI</span>
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-base font-extrabold text-white tracking-tight">Study With AI</h1>
              <span className="text-[10px] font-extrabold uppercase tracking-wider px-2 py-0.5 bg-emerald-500/20 text-emerald-400 border border-emerald-500/40 rounded-full">
                Android & Web
              </span>
            </div>
            <p className="text-[11px] text-slate-400">
              Interactive Live Preview & Source Code Reader
            </p>
          </div>
        </div>

        {/* View Switcher Controls */}
        <div className="flex items-center bg-slate-950 p-1 rounded-xl border border-slate-800">
          <button
            onClick={() => setViewMode('preview')}
            className={`px-3.5 py-1.5 rounded-lg text-xs font-bold flex items-center gap-2 transition ${
              viewMode === 'preview'
                ? 'bg-blue-600 text-white shadow-md'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            <Smartphone className="w-3.5 h-3.5" />
            <span className="hidden sm:inline">Run in Preview</span>
            <span className="sm:hidden">Preview</span>
          </button>

          <button
            onClick={() => setViewMode('code')}
            className={`px-3.5 py-1.5 rounded-lg text-xs font-bold flex items-center gap-2 transition ${
              viewMode === 'code'
                ? 'bg-blue-600 text-white shadow-md'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            <Code2 className="w-3.5 h-3.5" />
            <span className="hidden sm:inline">Read All Codes</span>
            <span className="sm:hidden">Code</span>
          </button>

          <button
            onClick={() => setViewMode('split')}
            className={`hidden xl:flex px-3.5 py-1.5 rounded-lg text-xs font-bold items-center gap-2 transition ${
              viewMode === 'split'
                ? 'bg-blue-600 text-white shadow-md'
                : 'text-slate-400 hover:text-white'
            }`}
          >
            <Layers className="w-3.5 h-3.5" />
            Split Screen
          </button>
        </div>

        {/* Action: Download directly into Android Phone from GitHub */}
        <div className="flex items-center gap-2">
          <button
            onClick={() => setIsDownloadModalOpen(true)}
            className="px-3.5 py-2 bg-gradient-to-r from-emerald-600 to-teal-500 hover:from-emerald-500 hover:to-teal-400 text-slate-950 font-extrabold text-xs rounded-xl shadow-lg shadow-emerald-500/20 flex items-center gap-2 transition"
          >
            <Download className="w-4 h-4" />
            <span className="hidden md:inline">Download to Phone (APK)</span>
            <span className="md:hidden">Get APK</span>
          </button>
        </div>
      </header>

      {/* GitHub Mobile Direct Download Quick Notice Bar */}
      <div className="bg-gradient-to-r from-blue-950/60 via-slate-900 to-indigo-950/60 border-b border-blue-500/20 px-4 py-2 flex items-center justify-between text-xs">
        <div className="flex items-center gap-2 text-slate-300">
          <span className="flex h-2 w-2 relative">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
            <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
          </span>
          <span className="font-semibold text-white">GitHub Direct Download:</span>
          <span className="text-slate-400 hidden sm:inline">
            Push code to GitHub &gt; GitHub Actions automatically compiles <code className="text-emerald-400 font-mono">app-debug.apk</code> &gt; Download directly onto your Android phone!
          </span>
        </div>
        <button
          onClick={() => setIsDownloadModalOpen(true)}
          className="text-blue-400 hover:text-blue-300 font-bold flex items-center gap-1 underline"
        >
          View 1-Click Guide &gt;
        </button>
      </div>

      {/* Main Container */}
      <main className="flex-1 p-2 sm:p-4 md:p-6 overflow-hidden flex flex-col">
        {viewMode === 'preview' && (
          <div className="flex-1 flex items-center justify-center overflow-y-auto">
            <PhoneSimulator 
              onOpenDownloadGuide={() => setIsDownloadModalOpen(true)}
              onOpenCodeReader={() => setViewMode('code')}
            />
          </div>
        )}

        {viewMode === 'code' && (
          <div className="flex-1 h-[calc(100vh-130px)]">
            <CodeReader />
          </div>
        )}

        {viewMode === 'split' && (
          <div className="flex-1 grid grid-cols-12 gap-6 h-[calc(100vh-130px)]">
            <div className="col-span-5 flex items-center justify-center overflow-y-auto">
              <PhoneSimulator 
                onOpenDownloadGuide={() => setIsDownloadModalOpen(true)}
                onOpenCodeReader={() => setViewMode('code')}
              />
            </div>
            <div className="col-span-7 h-full">
              <CodeReader />
            </div>
          </div>
        )}
      </main>

      {/* GitHub Download Modal */}
      <GitHubDownloadModal
        isOpen={isDownloadModalOpen}
        onClose={() => setIsDownloadModalOpen(false)}
      />
    </div>
  );
}
