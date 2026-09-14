import React, { useState, useMemo } from 'react';
import { 
  FileCode, Search, Copy, Check, Folder, ChevronRight, 
  ChevronDown, ExternalLink, Download, Code2, Sparkles, 
  Terminal, ShieldCheck, Filter
} from 'lucide-react';
import { androidCodeFiles, CodeFile } from '../data/androidFiles';

export const CodeReader: React.FC = () => {
  const [selectedFilePath, setSelectedFilePath] = useState<string>(androidCodeFiles[0].path);
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [selectedCategory, setSelectedCategory] = useState<string>('ALL');
  const [copied, setCopied] = useState<boolean>(false);

  const categories = ['ALL', 'Build & CI/CD', 'Entry & Manifest', 'UI Screens', 'Data & Database', 'AI & API', 'Utilities'];

  const filteredFiles = useMemo(() => {
    return androidCodeFiles.filter(file => {
      const matchesCategory = selectedCategory === 'ALL' || file.category === selectedCategory;
      const matchesSearch = searchQuery === '' || 
        file.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        file.path.toLowerCase().includes(searchQuery.toLowerCase()) ||
        file.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
        file.content.toLowerCase().includes(searchQuery.toLowerCase());
      return matchesCategory && matchesSearch;
    });
  }, [searchQuery, selectedCategory]);

  const currentFile = useMemo(() => {
    return androidCodeFiles.find(f => f.path === selectedFilePath) || androidCodeFiles[0];
  }, [selectedFilePath]);

  const handleCopy = () => {
    navigator.clipboard.writeText(currentFile.content);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleDownloadFile = () => {
    const blob = new Blob([currentFile.content], { type: 'text/plain;charset=utf-8' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = currentFile.name;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  };

  return (
    <div className="flex flex-col lg:flex-row h-full bg-slate-950 border border-slate-800 rounded-2xl overflow-hidden shadow-2xl">
      {/* Left Sidebar: File Tree & Selector */}
      <div className="w-full lg:w-80 border-b lg:border-b-0 lg:border-r border-slate-800 flex flex-col bg-slate-900/80">
        {/* Search & Header */}
        <div className="p-3 border-b border-slate-800 space-y-2">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-200 flex items-center gap-1.5 uppercase tracking-wider">
              <Code2 className="w-4 h-4 text-blue-400" />
              Source Code Explorer
            </span>
            <span className="text-[10px] px-2 py-0.5 rounded-full bg-blue-500/20 text-blue-400 font-mono">
              {filteredFiles.length} files
            </span>
          </div>

          <div className="relative">
            <Search className="w-3.5 h-3.5 absolute left-2.5 top-2.5 text-slate-500" />
            <input
              type="text"
              placeholder="Search code, file, class..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-8 pr-3 py-1.5 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 placeholder:text-slate-600 focus:outline-none focus:border-blue-500 transition"
            />
          </div>

          {/* Category Filter Pills */}
          <div className="flex gap-1 overflow-x-auto pb-1 text-[11px]">
            {categories.map(cat => (
              <button
                key={cat}
                onClick={() => setSelectedCategory(cat)}
                className={`px-2 py-1 rounded-md whitespace-nowrap transition ${
                  selectedCategory === cat
                    ? 'bg-blue-600 text-white font-bold'
                    : 'bg-slate-800/80 text-slate-400 hover:text-slate-200'
                }`}
              >
                {cat}
              </button>
            ))}
          </div>
        </div>

        {/* File List */}
        <div className="flex-1 overflow-y-auto p-2 space-y-1">
          {filteredFiles.map((file) => {
            const isSelected = file.path === currentFile.path;
            return (
              <button
                key={file.path}
                onClick={() => setSelectedFilePath(file.path)}
                className={`w-full text-left px-3 py-2 rounded-xl transition flex items-start gap-2.5 ${
                  isSelected
                    ? 'bg-blue-600/20 border border-blue-500/40 text-white'
                    : 'text-slate-400 hover:bg-slate-800/50 hover:text-slate-200'
                }`}
              >
                <FileCode className={`w-4 h-4 shrink-0 mt-0.5 ${isSelected ? 'text-blue-400' : 'text-slate-500'}`} />
                <div className="min-w-0 flex-1">
                  <div className="flex items-center justify-between gap-1">
                    <span className="text-xs font-semibold truncate text-slate-200">
                      {file.name}
                    </span>
                    <span className="text-[9px] uppercase tracking-wider px-1.5 py-0.2 bg-slate-800 rounded text-slate-400 font-mono shrink-0">
                      {file.language}
                    </span>
                  </div>
                  <p className="text-[10px] text-slate-500 truncate font-mono mt-0.5">
                    {file.path}
                  </p>
                </div>
              </button>
            );
          })}
        </div>
      </div>

      {/* Right Area: Code Display */}
      <div className="flex-1 flex flex-col min-w-0 bg-slate-950">
        {/* File Header Bar */}
        <div className="px-4 py-3 border-b border-slate-800 bg-slate-900/60 flex items-center justify-between gap-4">
          <div className="min-w-0 flex-1">
            <div className="flex items-center gap-2">
              <span className="text-xs font-bold text-white truncate font-mono">
                {currentFile.path}
              </span>
              <span className="text-[10px] px-2 py-0.5 rounded bg-blue-500/20 text-blue-400 font-bold border border-blue-500/30">
                {currentFile.category}
              </span>
            </div>
            <p className="text-xs text-slate-400 truncate mt-0.5">
              {currentFile.description}
            </p>
          </div>

          <div className="flex items-center gap-2 shrink-0">
            <button
              onClick={handleDownloadFile}
              title="Download file directly"
              className="p-1.5 bg-slate-800 hover:bg-slate-700 text-slate-300 rounded-lg text-xs flex items-center gap-1.5 transition"
            >
              <Download className="w-3.5 h-3.5" />
              <span className="hidden sm:inline text-xs">Save</span>
            </button>

            <button
              onClick={handleCopy}
              className={`px-3 py-1.5 rounded-lg text-xs font-bold flex items-center gap-1.5 transition ${
                copied
                  ? 'bg-emerald-500 text-slate-950'
                  : 'bg-blue-600 hover:bg-blue-500 text-white shadow-lg shadow-blue-500/20'
              }`}
            >
              {copied ? (
                <>
                  <Check className="w-3.5 h-3.5" />
                  Copied!
                </>
              ) : (
                <>
                  <Copy className="w-3.5 h-3.5" />
                  Copy Code
                </>
              )}
            </button>
          </div>
        </div>

        {/* Code Content with Line Numbers */}
        <div className="flex-1 overflow-auto p-4 text-xs font-mono leading-relaxed bg-slate-950 select-text">
          <pre className="text-slate-300 whitespace-pre">
            {currentFile.content.split('\n').map((line, idx) => (
              <div key={idx} className="flex hover:bg-slate-900/60 px-2 rounded">
                <span className="w-10 text-right pr-4 text-slate-600 select-none shrink-0 font-mono">
                  {idx + 1}
                </span>
                <span className="flex-1 text-slate-200">
                  {/* Basic syntax coloring highlights */}
                  {line.startsWith('import ') || line.startsWith('package ') ? (
                    <span className="text-purple-400">{line}</span>
                  ) : line.includes('class ') || line.includes('fun ') || line.includes('val ') || line.includes('var ') || line.includes('interface ') ? (
                    <span className="text-blue-300">{line}</span>
                  ) : line.startsWith('//') || line.startsWith('/*') || line.startsWith(' *') ? (
                    <span className="text-slate-500 italic">{line}</span>
                  ) : line.includes('@Composable') || line.includes('@Entity') || line.includes('@Dao') ? (
                    <span className="text-amber-400 font-semibold">{line}</span>
                  ) : line.includes('name:') || line.includes('runs-on:') || line.includes('steps:') || line.includes('uses:') ? (
                    <span className="text-emerald-400">{line}</span>
                  ) : (
                    line
                  )}
                </span>
              </div>
            ))}
          </pre>
        </div>
      </div>
    </div>
  );
};
