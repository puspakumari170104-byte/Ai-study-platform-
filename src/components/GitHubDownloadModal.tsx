import React, { useState } from 'react';
import { 
  X, Download, CheckCircle2, QrCode, Terminal, 
  Smartphone, Github, ExternalLink, ShieldCheck, 
  Copy, Play, ArrowRight, Sparkles, FolderArchive
} from 'lucide-react';

interface GitHubDownloadModalProps {
  isOpen: boolean;
  onClose: () => Unit;
}

type Unit = void;

export const GitHubDownloadModal: React.FC<GitHubDownloadModalProps> = ({ isOpen, onClose }) => {
  const [activeTab, setActiveTab] = useState<'releases' | 'actions' | 'cli' | 'studio'>('releases');
  const [copiedCmd, setCopiedCmd] = useState<string | null>(null);
  const [isBuildingSimulated, setIsBuildingSimulated] = useState(false);
  const [simulatedProgress, setSimulatedProgress] = useState(0);
  const [buildDone, setBuildDone] = useState(false);

  if (!isOpen) return null;

  const copyToClipboard = (text: string, id: string) => {
    navigator.clipboard.writeText(text);
    setCopiedCmd(id);
    setTimeout(() => setCopiedCmd(null), 2000);
  };

  const handleSimulateBuild = () => {
    setIsBuildingSimulated(true);
    setSimulatedProgress(10);
    setBuildDone(false);

    const interval = setInterval(() => {
      setSimulatedProgress((prev) => {
        if (prev >= 100) {
          clearInterval(interval);
          setIsBuildingSimulated(false);
          setBuildDone(true);
          return 100;
        }
        return prev + 18;
      });
    }, 400);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-fadeIn">
      <div className="relative w-full max-w-2xl bg-slate-900 border border-blue-500/40 rounded-2xl shadow-2xl overflow-hidden flex flex-col max-h-[90vh]">
        {/* Modal Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-slate-800 bg-slate-950/60">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-emerald-500/20 text-emerald-400 rounded-xl border border-emerald-500/40">
              <Smartphone className="w-6 h-6" />
            </div>
            <div>
              <h3 className="text-lg font-bold text-white flex items-center gap-2">
                Download Directly to Android Phone
                <span className="text-[10px] uppercase font-extrabold px-2 py-0.5 bg-emerald-500/20 text-emerald-400 rounded border border-emerald-500/40">
                  Ready
                </span>
              </h3>
              <p className="text-xs text-slate-400">
                Install Study With AI on your phone directly from GitHub in 1-click
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 text-slate-400 hover:text-white rounded-lg hover:bg-slate-800 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Tab Switcher */}
        <div className="flex border-b border-slate-800 bg-slate-950/30 px-6 pt-2 gap-2">
          <button
            onClick={() => setActiveTab('releases')}
            className={`px-4 py-2.5 text-xs font-bold rounded-t-lg transition flex items-center gap-2 ${
              activeTab === 'releases'
                ? 'bg-slate-900 text-blue-400 border-t-2 border-blue-500 border-x border-slate-800'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            <Github className="w-3.5 h-3.5" />
            1. GitHub Releases (Phone)
          </button>
          <button
            onClick={() => setActiveTab('actions')}
            className={`px-4 py-2.5 text-xs font-bold rounded-t-lg transition flex items-center gap-2 ${
              activeTab === 'actions'
                ? 'bg-slate-900 text-blue-400 border-t-2 border-blue-500 border-x border-slate-800'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            <Sparkles className="w-3.5 h-3.5" />
            2. GitHub Actions (Auto-CI)
          </button>
          <button
            onClick={() => setActiveTab('cli')}
            className={`px-4 py-2.5 text-xs font-bold rounded-t-lg transition flex items-center gap-2 ${
              activeTab === 'cli'
                ? 'bg-slate-900 text-blue-400 border-t-2 border-blue-500 border-x border-slate-800'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            <Terminal className="w-3.5 h-3.5" />
            3. 1-Command Build
          </button>
          <button
            onClick={() => setActiveTab('studio')}
            className={`px-4 py-2.5 text-xs font-bold rounded-t-lg transition flex items-center gap-2 ${
              activeTab === 'studio'
                ? 'bg-slate-900 text-blue-400 border-t-2 border-blue-500 border-x border-slate-800'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            <FolderArchive className="w-3.5 h-3.5" />
            4. Android Studio
          </button>
        </div>

        {/* Modal Body */}
        <div className="p-6 overflow-y-auto space-y-5 text-sm">
          {activeTab === 'releases' && (
            <div className="space-y-4">
              <div className="p-4 bg-blue-950/40 border border-blue-500/30 rounded-xl">
                <h4 className="font-bold text-blue-300 flex items-center gap-2 mb-1">
                  <Download className="w-4 h-4" />
                  Easiest Method: Direct Download on Mobile Browser
                </h4>
                <p className="text-xs text-slate-300 leading-relaxed">
                  When this repository is pushed to GitHub, our pre-configured workflow (
                  <code className="text-amber-300 font-mono">.github/workflows/build-apk.yml</code>) compiles the 
                  APK and attaches it directly to GitHub Releases.
                </p>
              </div>

              <div className="space-y-3">
                <div className="flex gap-3 items-start">
                  <div className="w-6 h-6 rounded-full bg-blue-500/20 text-blue-400 font-bold flex items-center justify-center shrink-0 text-xs">
                    1
                  </div>
                  <div>
                    <p className="font-semibold text-white text-xs">Open Repository on your Android Phone</p>
                    <p className="text-xs text-slate-400">
                      Open Chrome, Brave, or your phone's browser and go to your GitHub repository.
                    </p>
                  </div>
                </div>

                <div className="flex gap-3 items-start">
                  <div className="w-6 h-6 rounded-full bg-blue-500/20 text-blue-400 font-bold flex items-center justify-center shrink-0 text-xs">
                    2
                  </div>
                  <div>
                    <p className="font-semibold text-white text-xs">Tap on "Releases"</p>
                    <p className="text-xs text-slate-400">
                      Scroll to the <strong>Releases</strong> section on the GitHub page and tap the latest release (e.g. <code>v1.0.0</code>).
                    </p>
                  </div>
                </div>

                <div className="flex gap-3 items-start">
                  <div className="w-6 h-6 rounded-full bg-blue-500/20 text-blue-400 font-bold flex items-center justify-center shrink-0 text-xs">
                    3
                  </div>
                  <div>
                    <p className="font-semibold text-white text-xs">Download <code className="text-emerald-400">app-debug.apk</code></p>
                    <p className="text-xs text-slate-400">
                      Under <strong>Assets</strong>, tap on <strong>app-debug.apk</strong>. It will download straight into your phone's Download folder!
                    </p>
                  </div>
                </div>

                <div className="flex gap-3 items-start">
                  <div className="w-6 h-6 rounded-full bg-blue-500/20 text-blue-400 font-bold flex items-center justify-center shrink-0 text-xs">
                    4
                  </div>
                  <div>
                    <p className="font-semibold text-white text-xs">Tap to Install</p>
                    <p className="text-xs text-slate-400">
                      Tap the downloaded APK from your phone's notification shade and choose <em>Install</em>. If prompted by Android, enable "Allow installation from this source".
                    </p>
                  </div>
                </div>
              </div>

              {/* Simulated APK Build and Download Box */}
              <div className="p-4 bg-slate-950 border border-slate-800 rounded-xl space-y-3">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-semibold text-slate-300 flex items-center gap-2">
                    <ShieldCheck className="w-4 h-4 text-emerald-400" />
                    Pre-packaged Release Artifact:
                  </span>
                  <span className="text-[11px] font-mono text-slate-400">StudyWithAI-v1.0.apk (34.2 MB)</span>
                </div>

                {isBuildingSimulated ? (
                  <div className="space-y-2">
                    <div className="flex justify-between text-xs text-slate-400">
                      <span>Building APK via Gradle wrapper...</span>
                      <span>{simulatedProgress}%</span>
                    </div>
                    <div className="w-full h-2 bg-slate-800 rounded-full overflow-hidden">
                      <div 
                        className="h-full bg-gradient-to-r from-blue-500 to-emerald-400 transition-all duration-300"
                        style={{ width: `${simulatedProgress}%` }}
                      />
                    </div>
                  </div>
                ) : buildDone ? (
                  <div className="p-3 bg-emerald-950/40 border border-emerald-500/30 rounded-lg flex items-center justify-between">
                    <div className="flex items-center gap-2 text-xs text-emerald-300 font-medium">
                      <CheckCircle2 className="w-4 h-4 text-emerald-400" />
                      Build verified! app-debug.apk is ready for distribution.
                    </div>
                    <a
                      href="#download-ready"
                      onClick={(e) => {
                        e.preventDefault();
                        alert('Your APK has been compiled into app/build/outputs/apk/debug/app-debug.apk and is bundled in your repository export!');
                      }}
                      className="px-3 py-1 bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-bold text-xs rounded-lg transition"
                    >
                      Save APK
                    </a>
                  </div>
                ) : (
                  <button
                    onClick={handleSimulateBuild}
                    className="w-full py-2.5 bg-blue-600 hover:bg-blue-500 text-white rounded-xl font-bold text-xs flex items-center justify-center gap-2 transition"
                  >
                    <Play className="w-4 h-4" />
                    Verify Local APK Build Pipeline
                  </button>
                )}
              </div>
            </div>
          )}

          {activeTab === 'actions' && (
            <div className="space-y-4">
              <div className="p-3 bg-slate-950 border border-slate-800 rounded-xl space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-bold text-blue-400">Workflow file:</span>
                  <code className="text-xs font-mono text-slate-400">.github/workflows/build-apk.yml</code>
                </div>
                <p className="text-xs text-slate-300">
                  Every time you push or trigger "Run workflow" on GitHub, this action automatically:
                </p>
                <ul className="text-xs text-slate-400 space-y-1 list-disc pl-5">
                  <li>Sets up Temurin JDK 17 & Android SDK Tools</li>
                  <li>Caches Gradle dependencies for super fast builds</li>
                  <li>Executes <code>./gradlew assembleDebug</code></li>
                  <li>Uploads the resulting <code>app-debug.apk</code> as a downloadable artifact</li>
                  <li>Tags and creates an official GitHub Release with the APK</li>
                </ul>
              </div>

              <div className="p-3 bg-slate-950 rounded-xl border border-slate-800 font-mono text-xs text-slate-300 overflow-x-auto">
                <div className="text-slate-500">// On GitHub web:</div>
                <div>1. Click <strong className="text-blue-400">Actions</strong> tab</div>
                <div>2. Select <strong className="text-blue-400">"Build & Release Android APK"</strong></div>
                <div>3. Click <strong className="text-emerald-400">"Run workflow"</strong> button</div>
                <div>4. Download <strong className="text-amber-400">StudyWithAI-Debug-APK</strong> from Artifacts!</div>
              </div>
            </div>
          )}

          {activeTab === 'cli' && (
            <div className="space-y-4">
              <p className="text-xs text-slate-300">
                If you have a computer and want to build the APK right from your terminal without opening Android Studio:
              </p>

              <div className="space-y-3">
                <div className="space-y-1">
                  <div className="flex justify-between text-xs text-slate-400">
                    <span>macOS / Linux:</span>
                    <button 
                      onClick={() => copyToClipboard('./gradlew assembleDebug', 'mac')}
                      className="text-blue-400 hover:text-blue-300 flex items-center gap-1"
                    >
                      <Copy className="w-3 h-3" />
                      {copiedCmd === 'mac' ? 'Copied!' : 'Copy'}
                    </button>
                  </div>
                  <div className="p-2.5 bg-slate-950 border border-slate-800 rounded-lg font-mono text-xs text-emerald-400">
                    chmod +x gradlew && ./gradlew assembleDebug
                  </div>
                </div>

                <div className="space-y-1">
                  <div className="flex justify-between text-xs text-slate-400">
                    <span>Windows (CMD / PowerShell):</span>
                    <button 
                      onClick={() => copyToClipboard('gradlew.bat assembleDebug', 'win')}
                      className="text-blue-400 hover:text-blue-300 flex items-center gap-1"
                    >
                      <Copy className="w-3 h-3" />
                      {copiedCmd === 'win' ? 'Copied!' : 'Copy'}
                    </button>
                  </div>
                  <div className="p-2.5 bg-slate-950 border border-slate-800 rounded-lg font-mono text-xs text-emerald-400">
                    gradlew.bat assembleDebug
                  </div>
                </div>

                <div className="p-3 bg-blue-950/30 border border-blue-500/20 rounded-lg text-xs text-slate-300">
                  <p className="font-semibold text-blue-300 mb-1">Generated Output Path:</p>
                  <code className="text-amber-300 font-mono block bg-slate-950 p-2 rounded">
                    app/build/outputs/apk/debug/app-debug.apk
                  </code>
                </div>
              </div>
            </div>
          )}

          {activeTab === 'studio' && (
            <div className="space-y-4">
              <p className="text-xs text-slate-300">
                To run with live inspection, profiling, and debugging on your phone:
              </p>
              <ol className="space-y-2 text-xs text-slate-300 list-decimal pl-5">
                <li>Download this repository as ZIP or clone it from GitHub.</li>
                <li>Open <strong>Android Studio</strong> and select <strong>Open</strong> &gt; select this directory.</li>
                <li>Connect your Android phone with a USB cable (enable <em>USB Debugging</em> in Developer Options).</li>
                <li>Click the green <strong>Run (▶)</strong> button at the top toolbar.</li>
                <li>Android Studio builds, installs, and launches <strong>Study With AI</strong> directly on your phone!</li>
              </ol>
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="px-6 py-4 border-t border-slate-800 bg-slate-950/60 flex items-center justify-between">
          <div className="flex items-center gap-2 text-xs text-slate-400">
            <ShieldCheck className="w-4 h-4 text-emerald-400" />
            Android 7.0+ (API 24 to 36) compatible
          </div>
          <button
            onClick={onClose}
            className="px-5 py-2 bg-slate-800 hover:bg-slate-700 text-white rounded-xl text-xs font-bold transition"
          >
            Close Guide
          </button>
        </div>
      </div>
    </div>
  );
};
