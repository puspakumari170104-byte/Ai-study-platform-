import React, { useState, useEffect } from 'react';
import { 
  Home, BookOpen, Users, Dumbbell, Award, AlertCircle, 
  Sparkles, Play, Pause, Volume2, VolumeX, MessageSquare, 
  Check, X, ChevronRight, HelpCircle, Star, Flame, Shield, 
  Lock, ArrowRight, RotateCcw, Clock, Calendar, CheckCircle, 
  Send, ExternalLink, Lightbulb, Copy, Zap, Eye, Mic
} from 'lucide-react';
import { initialTeachers, practiceQuestions, initialMistakes, Teacher, Question, MistakeItem } from '../data/appData';

interface PhoneSimulatorProps {
  onOpenDownloadGuide: () => void;
  onOpenCodeReader: () => void;
}

export const PhoneSimulator: React.FC<PhoneSimulatorProps> = ({ onOpenDownloadGuide, onOpenCodeReader }) => {
  // Navigation
  const [activeTab, setActiveTab] = useState<'home' | 'classroom' | 'faculty' | 'practice' | 'tests' | 'mistakes' | 'plan' | 'pass'>('home');
  const [isHindi, setIsHindi] = useState(false);
  const [targetProgram, setTargetProgram] = useState<'NEET UG' | 'JEE Advanced' | 'UPSC CSE' | 'Class 12 Boards'>('NEET UG');

  // Teacher & Classroom
  const [currentTeacher, setCurrentTeacher] = useState<Teacher>(initialTeachers[0]);
  const [isPlayingLecture, setIsPlayingLecture] = useState(true);
  const [isSpeakingVoice, setIsSpeakingVoice] = useState(false);
  const [currentStep, setCurrentStep] = useState(1);
  const [teachingMethod, setTeachingMethod] = useState<'step' | 'analogy' | 'diagram' | 'numerical'>('step');
  const [pollSelected, setPollSelected] = useState<number | null>(null);
  const [interruptionText, setInterruptionText] = useState('');
  const [interruptionActive, setInterruptionActive] = useState(false);

  // Practice State
  const [practiceIndex, setPracticeIndex] = useState(0);
  const [selectedPracticeOption, setSelectedPracticeOption] = useState<number | null>(null);
  const [isPracticeChecked, setIsPracticeChecked] = useState(false);
  const [showHint, setShowHint] = useState(false);
  const [practiceMode, setPracticeMode] = useState<string>('Topic Practice');

  // Test State
  const [isTestActive, setIsTestActive] = useState(false);
  const [testQuestionIndex, setTestQuestionIndex] = useState(0);
  const [testAnswers, setTestAnswers] = useState<Record<number, number>>({});
  const [testTimer, setTestTimer] = useState(360);
  const [testResult, setTestResult] = useState<any | null>(null);

  // Mistakes State
  const [mistakes, setMistakes] = useState<MistakeItem[]>(initialMistakes);
  const [mistakeFilter, setMistakeFilter] = useState<'ALL' | 'UNRESOLVED' | 'TRAP'>('ALL');

  // Plan State
  const [missedDaysSimulated, setMissedDaysSimulated] = useState(false);

  // Subscription & UTR
  const [inputUtr, setInputUtr] = useState('');
  const [isPassActive, setIsPassActive] = useState(false);
  const [utrSuccess, setUtrSuccess] = useState(false);

  // AdMob Short Video Rewards Modal
  const [showAdMobModal, setShowAdMobModal] = useState(false);
  const [isWatchingAd, setIsWatchingAd] = useState(false);
  const [adCountdown, setAdCountdown] = useState(5);
  const [rewardToast, setRewardToast] = useState<string | null>(null);

  // Speech Synthesis for real voice lecture simulation
  const speakText = (text: string) => {
    if ('speechSynthesis' in window) {
      window.speechSynthesis.cancel();
      const utterance = new SpeechSynthesisUtterance(text);
      utterance.rate = 1.0;
      utterance.pitch = 1.0;
      utterance.onstart = () => setIsSpeakingVoice(true);
      utterance.onend = () => setIsSpeakingVoice(false);
      utterance.onerror = () => setIsSpeakingVoice(false);
      window.speechSynthesis.speak(utterance);
    } else {
      setIsSpeakingVoice(true);
      setTimeout(() => setIsSpeakingVoice(false), 3000);
    }
  };

  const stopVoice = () => {
    if ('speechSynthesis' in window) {
      window.speechSynthesis.cancel();
    }
    setIsSpeakingVoice(false);
  };

  // Test countdown timer
  useEffect(() => {
    let interval: any;
    if (isTestActive && testTimer > 0) {
      interval = setInterval(() => {
        setTestTimer(prev => {
          if (prev <= 1) {
            handleFinishTest();
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    }
    return () => clearInterval(interval);
  }, [isTestActive, testTimer]);

  const handleFinishTest = () => {
    setIsTestActive(false);
    let correct = 0;
    practiceQuestions.forEach((q, i) => {
      if (testAnswers[i] === q.correctIndex) correct++;
    });
    const score = correct * 4 - (Object.keys(testAnswers).length - correct) * 1;
    setTestResult({
      score: Math.max(score, 0),
      total: practiceQuestions.length * 4,
      accuracy: Math.round((correct / (Object.keys(testAnswers).length || 1)) * 100),
      percentile: 98.4,
      rank: 'AIR 1,120',
      correct,
      incorrect: Object.keys(testAnswers).length - correct,
    });
  };

  const handleSimulateWatchAd = (rewardTitle: string) => {
    setIsWatchingAd(true);
    setAdCountdown(5);
    const interval = setInterval(() => {
      setAdCountdown(prev => {
        if (prev <= 1) {
          clearInterval(interval);
          setIsWatchingAd(false);
          setShowAdMobModal(false);
          setRewardToast(`🎉 Reward Claimed: ${rewardTitle}!`);
          setTimeout(() => setRewardToast(null), 4000);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
  };

  const currentQ = practiceQuestions[practiceIndex] || practiceQuestions[0];

  return (
    <div className="flex flex-col items-center justify-center p-2 sm:p-4 w-full">
      {/* Phone Mockup Frame */}
      <div className="relative w-full max-w-[390px] h-[780px] bg-slate-950 border-[6px] border-slate-800 rounded-[44px] shadow-[0_25px_60px_-15px_rgba(0,0,0,0.9)] overflow-hidden flex flex-col select-none ring-1 ring-white/10">
        
        {/* Notch / Dynamic Island */}
        <div className="absolute top-2.5 left-1/2 -translate-x-1/2 w-28 h-4 bg-black rounded-full z-40 flex items-center justify-center">
          <div className="w-2.5 h-2.5 rounded-full bg-slate-900 border border-slate-700/60 mr-4" />
          <div className="w-2 h-2 rounded-full bg-blue-900/60" />
        </div>

        {/* Top Phone Status Bar */}
        <div className="h-10 pt-2 px-6 bg-slate-900 flex items-center justify-between text-[11px] font-semibold text-slate-300 z-30 select-none">
          <span>9:41</span>
          <div className="flex items-center gap-1.5 text-[10px]">
            <span>5G</span>
            <span>100%</span>
          </div>
        </div>

        {/* In-App Top Bar */}
        <div className="px-4 py-2.5 bg-slate-900 border-b border-slate-800 flex items-center justify-between z-20">
          <div className="flex items-center gap-2">
            <select
              value={targetProgram}
              onChange={(e) => setTargetProgram(e.target.value as any)}
              className="bg-slate-950 text-white text-xs font-bold px-2 py-1 rounded-lg border border-slate-700 focus:outline-none"
            >
              <option value="NEET UG">NEET UG</option>
              <option value="JEE Advanced">IIT JEE</option>
              <option value="UPSC CSE">UPSC IAS</option>
              <option value="Class 12 Boards">Class 12</option>
            </select>
          </div>

          <div className="flex items-center gap-1.5">
            {/* Language Toggle */}
            <button
              onClick={() => setIsHindi(!isHindi)}
              className="px-2 py-0.5 bg-blue-600/20 text-blue-400 border border-blue-500/40 rounded-full text-[10px] font-bold"
            >
              {isHindi ? 'हिंदी' : 'ENG'}
            </button>

            {/* AdMob Short Video Rewards Button */}
            <button
              onClick={() => setShowAdMobModal(true)}
              className="px-2 py-0.5 bg-amber-500/20 text-amber-300 border border-amber-500/50 rounded-full text-[10px] font-bold flex items-center gap-1"
            >
              <Star className="w-3 h-3 text-amber-400 fill-amber-400" />
              Rewards
            </button>

            {/* Streak */}
            <div className="px-2 py-0.5 bg-orange-500/20 text-orange-400 border border-orange-500/40 rounded-full text-[10px] font-bold flex items-center gap-0.5">
              <Flame className="w-3 h-3 text-orange-400 fill-orange-400" />
              14d
            </div>
          </div>
        </div>

        {/* Reward Toast Banner */}
        {rewardToast && (
          <div className="bg-emerald-600 text-white text-xs font-bold py-1.5 px-3 text-center animate-bounce z-40">
            {rewardToast}
          </div>
        )}

        {/* Main Screen Content Area */}
        <div className="flex-1 overflow-y-auto bg-slate-950 text-white p-3.5 space-y-4">
          {/* 1. DASHBOARD VIEW */}
          {activeTab === 'home' && (
            <div className="space-y-3.5 animate-fadeIn">
              {/* Today's Mission */}
              <div className="p-4 bg-gradient-to-br from-slate-900 to-slate-900/90 border border-blue-500/30 rounded-2xl shadow-lg relative overflow-hidden">
                <div className="flex justify-between items-center mb-1.5">
                  <span className="text-[10px] font-extrabold uppercase tracking-wider px-2 py-0.5 bg-blue-600 text-white rounded">
                    {isHindi ? 'आज का मुख्य लक्ष्य' : "TODAY'S MISSION"}
                  </span>
                  <span className="text-[11px] text-slate-400 font-medium">45 / 90 mins</span>
                </div>
                <h3 className="text-base font-bold text-white">Laws of Motion & Friction</h3>
                <p className="text-xs text-slate-300 mt-0.5 leading-snug">
                  {isHindi 
                    ? '3-बॉडी पुली और फ्रिक्शन ट्रैप्स पर कोटा मास्टरक्लास और 5 अभ्यास प्रश्न हल करें।'
                    : 'Master 3-body pulley tension equations and solve 5 high-yield numericals with Dr. Vikram Seth.'}
                </p>
                <div className="w-full bg-slate-800 h-1.5 rounded-full mt-3 overflow-hidden">
                  <div className="w-1/2 h-full bg-gradient-to-r from-blue-500 to-emerald-400" />
                </div>
                <button
                  onClick={() => {
                    setActiveTab('classroom');
                    speakText(currentTeacher.speechAudioText);
                  }}
                  className="w-full mt-3 py-2.5 bg-emerald-500 hover:bg-emerald-400 text-slate-950 rounded-xl font-bold text-xs flex items-center justify-center gap-2 shadow-lg shadow-emerald-500/20 transition"
                >
                  <Play className="w-3.5 h-3.5 fill-current" />
                  {isHindi ? 'पढ़ाई जारी रखें (LIVE CLASS)' : 'CONTINUE LEARNING'}
                </button>
              </div>

              {/* Up Next: 4K Masterclass Alert */}
              <div className="p-3 bg-slate-900 border border-slate-800 rounded-xl flex items-center justify-between">
                <div className="flex items-center gap-2.5">
                  <div className="w-9 h-9 rounded-full bg-blue-600/20 text-blue-400 font-bold flex items-center justify-center text-xs border border-blue-500/40">
                    VS
                  </div>
                  <div>
                    <div className="flex items-center gap-1.5">
                      <span className="text-[10px] font-bold text-emerald-400 uppercase tracking-wider">● Live Now</span>
                      <span className="text-[10px] text-slate-500">45 mins</span>
                    </div>
                    <p className="text-xs font-bold text-slate-200">Work, Energy & Power Sprint</p>
                  </div>
                </div>
                <button
                  onClick={() => {
                    setActiveTab('classroom');
                    speakText(currentTeacher.speechAudioText);
                  }}
                  className="px-2.5 py-1 bg-blue-600 hover:bg-blue-500 text-white rounded-lg text-xs font-bold"
                >
                  Join
                </button>
              </div>

              {/* Core Feature Hub Grid */}
              <div className="grid grid-cols-2 gap-2.5">
                <button
                  onClick={() => setActiveTab('classroom')}
                  className="p-3 bg-slate-900 hover:bg-slate-850 border border-slate-800 rounded-xl text-left transition"
                >
                  <div className="p-2 w-8 h-8 rounded-lg bg-blue-500/20 text-blue-400 mb-2 flex items-center justify-center">
                    <BookOpen className="w-4 h-4" />
                  </div>
                  <h4 className="text-xs font-bold text-white">4K AI Classroom</h4>
                  <p className="text-[10px] text-slate-400 mt-0.5">Voice & Digital Board</p>
                </button>

                <button
                  onClick={() => setActiveTab('practice')}
                  className="p-3 bg-slate-900 hover:bg-slate-850 border border-slate-800 rounded-xl text-left transition"
                >
                  <div className="p-2 w-8 h-8 rounded-lg bg-emerald-500/20 text-emerald-400 mb-2 flex items-center justify-center">
                    <Dumbbell className="w-4 h-4" />
                  </div>
                  <h4 className="text-xs font-bold text-white">Daily Practice</h4>
                  <p className="text-[10px] text-slate-400 mt-0.5">Adaptive 9 Modes</p>
                </button>

                <button
                  onClick={() => setActiveTab('tests')}
                  className="p-3 bg-slate-900 hover:bg-slate-850 border border-slate-800 rounded-xl text-left transition"
                >
                  <div className="p-2 w-8 h-8 rounded-lg bg-purple-500/20 text-purple-400 mb-2 flex items-center justify-center">
                    <Award className="w-4 h-4" />
                  </div>
                  <h4 className="text-xs font-bold text-white">Mock Test Center</h4>
                  <p className="text-[10px] text-slate-400 mt-0.5">NTA Real Timer</p>
                </button>

                <button
                  onClick={() => setActiveTab('mistakes')}
                  className="p-3 bg-slate-900 hover:bg-slate-850 border border-slate-800 rounded-xl text-left transition"
                >
                  <div className="p-2 w-8 h-8 rounded-lg bg-amber-500/20 text-amber-400 mb-2 flex items-center justify-center">
                    <AlertCircle className="w-4 h-4" />
                  </div>
                  <h4 className="text-xs font-bold text-white">Mistakes Book</h4>
                  <p className="text-[10px] text-slate-400 mt-0.5">2 Traps to Revise</p>
                </button>
              </div>

              {/* AdMob Short Video Rewards Banner */}
              <div 
                onClick={() => setShowAdMobModal(true)}
                className="p-3 bg-gradient-to-r from-amber-950/50 via-slate-900 to-indigo-950/40 border border-amber-500/40 rounded-xl flex items-center justify-between cursor-pointer hover:border-amber-400 transition"
              >
                <div className="flex items-center gap-2.5">
                  <div className="w-8 h-8 rounded-full bg-amber-500/20 flex items-center justify-center text-amber-400">
                    <Star className="w-4 h-4 fill-amber-400" />
                  </div>
                  <div>
                    <div className="flex items-center gap-1.5">
                      <span className="text-xs font-bold text-white">Short Video Rewards</span>
                      <span className="text-[9px] font-extrabold px-1.5 py-0.2 bg-amber-500 text-slate-950 rounded">FREE</span>
                    </div>
                    <p className="text-[10px] text-slate-400">Watch 30s video to unlock 4K Passes & Doubt Credits</p>
                  </div>
                </div>
                <button className="px-2.5 py-1 bg-amber-500 text-slate-950 font-bold text-xs rounded-lg">
                  Watch
                </button>
              </div>

              {/* Direct GitHub Phone Download Banner */}
              <div 
                onClick={onOpenDownloadGuide}
                className="p-3 bg-blue-950/40 border border-blue-500/30 rounded-xl flex items-center justify-between cursor-pointer hover:border-blue-400 transition"
              >
                <div className="flex items-center gap-2.5">
                  <div className="w-8 h-8 rounded-full bg-blue-500/20 flex items-center justify-center text-blue-400">
                    <Smartphone className="w-4 h-4" />
                  </div>
                  <div>
                    <h5 className="text-xs font-bold text-white">Download APK to Real Phone</h5>
                    <p className="text-[10px] text-slate-400">Via GitHub Releases & Actions</p>
                  </div>
                </div>
                <ArrowRight className="w-4 h-4 text-blue-400" />
              </div>
            </div>
          )}

          {/* 2. CLASSROOM VIEW */}
          {activeTab === 'classroom' && (
            <div className="space-y-3 animate-fadeIn">
              {/* Classroom Header */}
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="text-sm font-bold text-white">Laws of Motion & Friction</h3>
                  <p className="text-[10px] text-slate-400">{currentTeacher.name} • Live Kota Studio</p>
                </div>
                <button
                  onClick={() => {
                    if (isSpeakingVoice) stopVoice();
                    else speakText(currentTeacher.speechAudioText);
                  }}
                  className={`p-2 rounded-xl text-xs flex items-center gap-1 transition ${
                    isSpeakingVoice ? 'bg-amber-500 text-slate-950' : 'bg-slate-800 text-slate-300'
                  }`}
                >
                  {isSpeakingVoice ? <Volume2 className="w-3.5 h-3.5 animate-pulse" /> : <VolumeX className="w-3.5 h-3.5" />}
                  <span className="text-[10px] font-bold">{isSpeakingVoice ? 'Speaking' : 'Muted'}</span>
                </button>
              </div>

              {/* 4K Real Character Video Player & Teacher Stage */}
              <div className="relative aspect-video w-full bg-slate-900 rounded-xl border border-slate-800 overflow-hidden shadow-inner flex flex-col justify-end p-3">
                <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-slate-950/20 to-transparent z-10" />
                
                {/* Visual Avatar Graphic */}
                <div className="absolute inset-0 flex items-center justify-center">
                  <div className="relative">
                    <div className="w-24 h-24 rounded-full bg-blue-500/20 border-2 border-blue-400/40 flex items-center justify-center shadow-2xl">
                      <span className="text-2xl font-black text-blue-400 font-mono">
                        {currentTeacher.name.split(' ').map(n => n[0]).join('')}
                      </span>
                    </div>
                    {isSpeakingVoice && (
                      <div className="absolute -bottom-1 left-1/2 -translate-x-1/2 px-2 py-0.5 bg-amber-500 text-slate-950 rounded-full text-[9px] font-bold uppercase tracking-wider flex items-center gap-1 shadow">
                        <Zap className="w-2.5 h-2.5 fill-current" />
                        AI Voice Live
                      </div>
                    )}
                  </div>
                </div>

                {/* Subtitle Bar */}
                <div className="relative z-20 bg-black/80 backdrop-blur-md p-2 rounded-lg border border-white/10 text-[11px] text-slate-200 leading-snug">
                  {currentTeacher.speechAudioText}
                </div>
              </div>

              {/* Interactive Digital Whiteboard */}
              <div className="p-3 bg-[#132B20] border-2 border-[#2E5A44] rounded-xl space-y-2">
                <div className="flex items-center justify-between text-[10px] text-emerald-300 font-bold uppercase tracking-wider border-b border-[#2E5A44] pb-1">
                  <span>Smart Blackboard Derivations</span>
                  <span className="text-amber-300 font-mono">Step {currentStep} of 3</span>
                </div>
                <div className="font-mono text-xs text-emerald-100 bg-black/40 p-2.5 rounded-lg border border-emerald-500/20">
                  {currentTeacher.chalkboardEquation}
                </div>
                <p className="text-[11px] text-emerald-200/90 leading-tight">
                  • Downward force component: <span className="text-amber-300 font-bold">mg·sin θ</span><br />
                  • Normal floor reaction: <span className="text-emerald-400 font-bold">N = mg·cos θ</span><br />
                  • Maximum static friction grip: <span className="text-purple-300 font-bold">f_max = μs·N</span>
                </p>
              </div>

              {/* Socratic "I don't understand" Method Switcher */}
              <div className="p-2.5 bg-slate-900 border border-slate-800 rounded-xl space-y-1.5">
                <span className="text-[10px] font-bold text-blue-400 uppercase tracking-wider">
                  I Don't Understand (Switch Pedagogy):
                </span>
                <div className="grid grid-cols-2 gap-1.5 text-[11px]">
                  <button 
                    onClick={() => {
                      setTeachingMethod('analogy');
                      speakText("Don't worry! Think of parking a bicycle on a steep ramp. If the road is smooth ice, it slides. If it's rough asphalt, friction holds it. That's the balance!");
                    }}
                    className={`p-1.5 rounded-lg text-left border transition ${
                      teachingMethod === 'analogy' ? 'bg-blue-600/30 border-blue-500 text-white font-bold' : 'bg-slate-950 border-slate-800 text-slate-400'
                    }`}
                  >
                    🚗 Real-World Analogy
                  </button>
                  <button 
                    onClick={() => {
                      setTeachingMethod('numerical');
                      speakText("Let's put real numbers! Say mass is 2 kg and angle is 30 degrees. Downward pull is 9.8 Newtons, while maximum friction is 10.18 Newtons. It does not move!");
                    }}
                    className={`p-1.5 rounded-lg text-left border transition ${
                      teachingMethod === 'numerical' ? 'bg-blue-600/30 border-blue-500 text-white font-bold' : 'bg-slate-950 border-slate-800 text-slate-400'
                    }`}
                  >
                    🔢 Concrete Numbers
                  </button>
                </div>
              </div>

              {/* In-Class Comprehension Poll */}
              <div className="p-3 bg-slate-900 border border-slate-800 rounded-xl space-y-2">
                <div className="flex justify-between items-center text-[10px] font-bold text-amber-400 uppercase tracking-wider">
                  <span>Live Concept Check</span>
                  {pollSelected !== null && <span className="text-emerald-400">Answer Recorded!</span>}
                </div>
                <p className="text-xs font-semibold text-white">
                  If pulling force is 4 N and limiting friction is 10 N, what is the friction force?
                </p>
                <div className="grid grid-cols-2 gap-1.5 text-xs">
                  {['4 N (Matches Force)', '10 N (Maximum)', '0 N (No Friction)', '6 N (Difference)'].map((opt, i) => (
                    <button
                      key={i}
                      onClick={() => {
                        setPollSelected(i);
                        if (i === 0) {
                          speakText("Spot on! Static friction matches the applied force exactly up to 10 N.");
                        } else {
                          speakText("Watch out! That's an examiner trap. Static friction is self-adjusting!");
                        }
                      }}
                      className={`p-2 rounded-lg text-left border transition font-medium text-[11px] ${
                        pollSelected === i
                          ? i === 0 ? 'bg-emerald-600/30 border-emerald-500 text-white' : 'bg-red-600/30 border-red-500 text-white'
                          : 'bg-slate-950 border-slate-800 text-slate-300'
                      }`}
                    >
                      {opt}
                    </button>
                  ))}
                </div>
              </div>

              {/* Verbal / Text Interruption Input */}
              <div className="flex items-center gap-2">
                <input
                  type="text"
                  placeholder="Interrupt or ask doubt..."
                  value={interruptionText}
                  onChange={(e) => setInterruptionText(e.target.value)}
                  className="flex-1 bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-xs text-white placeholder:text-slate-500 focus:outline-none focus:border-blue-500"
                />
                <button
                  onClick={() => {
                    if (interruptionText) {
                      speakText(`Hold on, that's a great question: ${interruptionText}. Let's address this foundational rule!`);
                      setInterruptionText('');
                    }
                  }}
                  className="p-2 bg-blue-600 hover:bg-blue-500 text-white rounded-xl text-xs font-bold"
                >
                  <Send className="w-3.5 h-3.5" />
                </button>
              </div>
            </div>
          )}

          {/* 3. FACULTY DIRECTORY VIEW */}
          {activeTab === 'faculty' && (
            <div className="space-y-3 animate-fadeIn">
              <div className="flex justify-between items-center">
                <h3 className="text-sm font-bold text-white">AI Faculty Directory</h3>
                <span className="text-[10px] text-blue-400 font-mono font-bold">Kota & NCERT Faculty</span>
              </div>

              <div className="space-y-2.5">
                {initialTeachers.map((teacher) => (
                  <div
                    key={teacher.id}
                    onClick={() => {
                      setCurrentTeacher(teacher);
                      setActiveTab('classroom');
                      speakText(teacher.speechAudioText);
                    }}
                    className={`p-3 rounded-xl border transition cursor-pointer ${
                      currentTeacher.id === teacher.id
                        ? 'bg-blue-950/40 border-blue-500'
                        : 'bg-slate-900 border-slate-800 hover:border-slate-700'
                    }`}
                  >
                    <div className="flex items-start justify-between">
                      <div className="flex items-center gap-3">
                        <div 
                          className="w-10 h-10 rounded-xl flex items-center justify-center font-bold text-sm text-white"
                          style={{ backgroundColor: teacher.avatarColor }}
                        >
                          {teacher.name.split(' ').map(n => n[0]).join('')}
                        </div>
                        <div>
                          <h4 className="text-xs font-bold text-white">{teacher.name}</h4>
                          <p className="text-[10px] text-blue-400 font-semibold">{teacher.subject} • {teacher.title}</p>
                          <p className="text-[10px] text-slate-400 mt-0.5">{teacher.teachingStyle}</p>
                        </div>
                      </div>
                      <div className="flex items-center gap-1 text-[11px] font-bold text-amber-400">
                        <Star className="w-3 h-3 fill-current" />
                        {teacher.rating}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* 4. PRACTICE VIEW */}
          {activeTab === 'practice' && (
            <div className="space-y-3.5 animate-fadeIn">
              {/* Practice Header & Mode Tabs */}
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-sm font-bold text-white">Adaptive Practice Engine</h3>
                  <p className="text-[10px] text-slate-400">{currentQ.chapter}</p>
                </div>
                <span className="px-2 py-0.5 bg-purple-500/20 text-purple-300 rounded-md text-[10px] font-bold border border-purple-500/40">
                  {currentQ.difficulty}
                </span>
              </div>

              {/* Progress & Marking Scheme */}
              <div className="flex justify-between items-center text-[11px] text-slate-400">
                <span>Question {practiceIndex + 1} of {practiceQuestions.length}</span>
                <span className="text-amber-400 font-bold">+4 Correct / -1 Negative</span>
              </div>

              {/* Question Card */}
              <div className="p-3.5 bg-slate-900 border border-slate-800 rounded-xl space-y-2">
                <p className="text-xs font-semibold text-slate-100 leading-relaxed">
                  {currentQ.text}
                </p>
              </div>

              {/* Options */}
              <div className="space-y-2">
                {currentQ.options.map((opt, i) => {
                  const isSelected = selectedPracticeOption === i;
                  const isCorrect = i === currentQ.correctIndex;
                  return (
                    <button
                      key={i}
                      disabled={isPracticeChecked}
                      onClick={() => setSelectedPracticeOption(i)}
                      className={`w-full p-2.5 rounded-xl border text-left text-xs transition flex items-center justify-between ${
                        isPracticeChecked
                          ? isCorrect 
                            ? 'bg-emerald-600/30 border-emerald-500 text-white font-bold'
                            : isSelected ? 'bg-red-600/30 border-red-500 text-white' : 'bg-slate-900 border-slate-800 text-slate-400'
                          : isSelected 
                            ? 'bg-blue-600/30 border-blue-500 text-white font-bold'
                            : 'bg-slate-900 border-slate-800 text-slate-300 hover:border-slate-700'
                      }`}
                    >
                      <span>{String.fromCharCode(65 + i)}. {opt}</span>
                      {isPracticeChecked && isCorrect && <Check className="w-4 h-4 text-emerald-400" />}
                      {isPracticeChecked && isSelected && !isCorrect && <X className="w-4 h-4 text-red-400" />}
                    </button>
                  );
                })}
              </div>

              {/* Action Buttons: Check Answer & Socratic Hint */}
              <div className="space-y-2">
                {!isPracticeChecked ? (
                  <div className="flex gap-2">
                    <button
                      onClick={() => setShowHint(!showHint)}
                      className="px-3 py-2 bg-amber-500/20 text-amber-400 rounded-xl text-xs font-bold flex items-center gap-1.5 border border-amber-500/30"
                    >
                      <Lightbulb className="w-3.5 h-3.5" />
                      {showHint ? 'Hide Hint' : 'Hint'}
                    </button>
                    <button
                      disabled={selectedPracticeOption === null}
                      onClick={() => {
                        setIsPracticeChecked(true);
                        if (selectedPracticeOption !== currentQ.correctIndex) {
                          // Log new mistake into Mistake Notebook
                          setMistakes(prev => [
                            {
                              id: `m_${Date.now()}`,
                              chapter: currentQ.chapter,
                              question: currentQ.text,
                              userAnswer: currentQ.options[selectedPracticeOption!],
                              correctAnswer: currentQ.options[currentQ.correctIndex],
                              cause: currentQ.mistakeType,
                              revisionCount: 0,
                              isResolved: false
                            },
                            ...prev
                          ]);
                        }
                      }}
                      className="flex-1 py-2 bg-blue-600 hover:bg-blue-500 disabled:opacity-50 text-white rounded-xl text-xs font-bold transition"
                    >
                      Check Answer
                    </button>
                  </div>
                ) : (
                  <button
                    onClick={() => {
                      setPracticeIndex((prev) => (prev + 1) % practiceQuestions.length);
                      setSelectedPracticeOption(null);
                      setIsPracticeChecked(false);
                      setShowHint(false);
                    }}
                    className="w-full py-2.5 bg-blue-600 hover:bg-blue-500 text-white rounded-xl text-xs font-bold transition flex items-center justify-center gap-2"
                  >
                    <span>Next Question</span>
                    <ArrowRight className="w-3.5 h-3.5" />
                  </button>
                )}

                {/* Socratic Hint Box */}
                {showHint && (
                  <div className="p-3 bg-amber-950/40 border border-amber-500/40 rounded-xl text-xs text-amber-200">
                    💡 <strong>Socratic Hint:</strong> {currentQ.hint}
                  </div>
                )}

                {/* Explanation Card when checked */}
                {isPracticeChecked && (
                  <div className={`p-3 rounded-xl border text-xs space-y-1.5 ${
                    selectedPracticeOption === currentQ.correctIndex 
                      ? 'bg-emerald-950/40 border-emerald-500/40 text-emerald-200'
                      : 'bg-red-950/40 border-red-500/40 text-red-200'
                  }`}>
                    <div className="font-bold flex items-center justify-between">
                      <span>{selectedPracticeOption === currentQ.correctIndex ? '✓ Correct! (+4 Marks)' : '✕ Exam Trap! (-1 Mark)'}</span>
                      <span className="text-[10px] px-1.5 py-0.5 bg-black/40 rounded font-normal">{currentQ.mistakeType}</span>
                    </div>
                    <p className="text-slate-300 leading-relaxed">{currentQ.explanation}</p>
                    <div className="font-mono text-[11px] text-amber-300 bg-black/40 p-1.5 rounded">
                      Formula: {currentQ.formula}
                    </div>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* 5. TEST CENTER VIEW */}
          {activeTab === 'tests' && (
            <div className="space-y-3.5 animate-fadeIn">
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-sm font-bold text-white">NTA Pattern Mock Tests</h3>
                  <p className="text-[10px] text-slate-400">Timed Simulated National Benchmarks</p>
                </div>
              </div>

              {!isTestActive && !testResult ? (
                <div className="space-y-3">
                  <div className="p-4 bg-gradient-to-br from-slate-900 to-indigo-950/50 border border-indigo-500/30 rounded-2xl space-y-3">
                    <span className="text-[10px] font-bold px-2 py-0.5 bg-indigo-600 text-white rounded">
                      All-India Diagnostic Series
                    </span>
                    <h4 className="text-sm font-bold text-white">Laws of Motion & Mechanics Sprint Mock</h4>
                    <p className="text-xs text-slate-300">
                      4 Questions • 6 Minutes • +4 Marks / -1 Penalty • AIR & Percentile Predictor
                    </p>
                    <button
                      onClick={() => {
                        setIsTestActive(true);
                        setTestTimer(360);
                        setTestAnswers({});
                        setTestResult(null);
                      }}
                      className="w-full py-2.5 bg-blue-600 hover:bg-blue-500 text-white rounded-xl text-xs font-bold transition flex items-center justify-center gap-2"
                    >
                      <Play className="w-3.5 h-3.5 fill-current" />
                      Begin Timed Exam
                    </button>
                  </div>
                </div>
              ) : isTestActive ? (
                /* Active Test View */
                <div className="space-y-3">
                  <div className="flex justify-between items-center p-2.5 bg-slate-900 rounded-xl border border-slate-800">
                    <span className="text-xs font-bold text-white">Question {testQuestionIndex + 1} of {practiceQuestions.length}</span>
                    <span className="font-mono text-xs font-bold text-amber-400 flex items-center gap-1">
                      <Clock className="w-3.5 h-3.5" />
                      {Math.floor(testTimer / 60)}:{String(testTimer % 60).padStart(2, '0')}
                    </span>
                  </div>

                  <div className="p-3 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-200">
                    {practiceQuestions[testQuestionIndex].text}
                  </div>

                  <div className="space-y-1.5">
                    {practiceQuestions[testQuestionIndex].options.map((opt, i) => {
                      const isChosen = testAnswers[testQuestionIndex] === i;
                      return (
                        <button
                          key={i}
                          onClick={() => setTestAnswers(prev => ({ ...prev, [testQuestionIndex]: i }))}
                          className={`w-full p-2.5 rounded-xl border text-left text-xs transition ${
                            isChosen ? 'bg-blue-600/30 border-blue-500 text-white font-bold' : 'bg-slate-900 border-slate-800 text-slate-300'
                          }`}
                        >
                          {String.fromCharCode(65 + i)}. {opt}
                        </button>
                      );
                    })}
                  </div>

                  <div className="flex gap-2 pt-2">
                    <button
                      disabled={testQuestionIndex === 0}
                      onClick={() => setTestQuestionIndex(prev => prev - 1)}
                      className="flex-1 py-2 bg-slate-900 border border-slate-800 text-slate-300 text-xs font-bold rounded-xl disabled:opacity-40"
                    >
                      Previous
                    </button>
                    {testQuestionIndex < practiceQuestions.length - 1 ? (
                      <button
                        onClick={() => setTestQuestionIndex(prev => prev + 1)}
                        className="flex-1 py-2 bg-slate-800 text-white text-xs font-bold rounded-xl"
                      >
                        Next
                      </button>
                    ) : (
                      <button
                        onClick={handleFinishTest}
                        className="flex-1 py-2 bg-emerald-600 hover:bg-emerald-500 text-white text-xs font-bold rounded-xl"
                      >
                        Submit Test
                      </button>
                    )}
                  </div>
                </div>
              ) : (
                /* Test Result Analysis View */
                <div className="space-y-3 animate-fadeIn">
                  <div className="p-4 bg-slate-900 border border-emerald-500/40 rounded-2xl text-center space-y-1.5">
                    <span className="text-[10px] font-bold uppercase tracking-wider px-2 py-0.5 bg-emerald-500/20 text-emerald-400 rounded">
                      Evaluation Complete
                    </span>
                    <h3 className="text-3xl font-black text-white font-mono">{testResult.score} / {testResult.total}</h3>
                    <p className="text-xs text-slate-400 font-medium">Predicted Percentile: <strong className="text-amber-400">{testResult.percentile}%ile</strong> • {testResult.rank}</p>
                    <div className="grid grid-cols-2 gap-2 pt-2 text-xs">
                      <div className="p-2 bg-slate-950 rounded-lg">
                        <span className="text-slate-400 block text-[10px]">Accuracy</span>
                        <span className="text-emerald-400 font-bold">{testResult.accuracy}%</span>
                      </div>
                      <div className="p-2 bg-slate-950 rounded-lg">
                        <span className="text-slate-400 block text-[10px]">Correct / Incorrect</span>
                        <span className="text-white font-bold">{testResult.correct} / {testResult.incorrect}</span>
                      </div>
                    </div>
                  </div>

                  <button
                    onClick={() => setActiveTab('mistakes')}
                    className="w-full py-2.5 bg-blue-600 hover:bg-blue-500 text-white rounded-xl text-xs font-bold flex items-center justify-center gap-2"
                  >
                    <span>Open Mistakes Book ({testResult.incorrect} slips logged)</span>
                    <ArrowRight className="w-3.5 h-3.5" />
                  </button>
                </div>
              )}
            </div>
          )}

          {/* 6. MISTAKES BOOK VIEW */}
          {activeTab === 'mistakes' && (
            <div className="space-y-3 animate-fadeIn">
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-sm font-bold text-white">My Mistakes Book</h3>
                  <p className="text-[10px] text-slate-400">Cognitive Root-Cause Diagnosis</p>
                </div>
                <button
                  onClick={() => setActiveTab('practice')}
                  className="px-2.5 py-1 bg-amber-500 text-slate-950 rounded-lg text-xs font-bold"
                >
                  Retest All
                </button>
              </div>

              <div className="space-y-2.5">
                {mistakes.map((m) => (
                  <div key={m.id} className="p-3 bg-slate-900 border border-slate-800 rounded-xl space-y-1.5">
                    <div className="flex justify-between items-center">
                      <span className="text-[10px] font-bold text-blue-400">{m.chapter}</span>
                      <span className="text-[9px] px-2 py-0.5 bg-red-500/20 text-red-400 rounded-full font-bold">
                        {m.cause}
                      </span>
                    </div>
                    <p className="text-xs text-slate-200">{m.question}</p>
                    <div className="grid grid-cols-2 gap-2 text-[11px] pt-1">
                      <div className="p-1.5 bg-red-950/40 rounded border border-red-500/20 text-red-300">
                        Your Slip: {m.userAnswer}
                      </div>
                      <div className="p-1.5 bg-emerald-950/40 rounded border border-emerald-500/20 text-emerald-300 font-semibold">
                        Correct: {m.correctAnswer}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* 7. STUDY PLAN VIEW */}
          {activeTab === 'plan' && (
            <div className="space-y-3 animate-fadeIn">
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-sm font-bold text-white">Personalized Study Plan</h3>
                  <p className="text-[10px] text-slate-400">Dynamic Syllabus Balancing Engine</p>
                </div>
                <button
                  onClick={() => setMissedDaysSimulated(!missedDaysSimulated)}
                  className="px-2 py-1 bg-slate-800 text-slate-300 rounded-lg text-[10px] font-bold"
                >
                  {missedDaysSimulated ? 'Reset' : 'Simulate 3 Days Missed'}
                </button>
              </div>

              {missedDaysSimulated && (
                <div className="p-3 bg-amber-950/50 border border-amber-500/40 rounded-xl text-xs text-amber-200">
                  ⚠️ <strong>Dynamic Reschedule:</strong> You missed 3 days. Our AI Mentor re-balanced your syllabus milestones across 3 buffer days without compromising high-yield chapters!
                </div>
              )}

              <div className="space-y-2">
                {[
                  { time: '06:30 - 08:30', title: 'Deep Physics Concept Derivations', sub: 'Newtonian Laws & Vectors' },
                  { time: '09:30 - 11:00', title: 'Adaptive Practice Session (DPP)', sub: '15 Target Numericals' },
                  { time: '15:00 - 16:30', title: 'Mistakes Book Spaced Revision', sub: 'Trap Re-Derivations' },
                  { time: '19:30 - 20:45', title: 'Timed Speed Diagnostic Assessment', sub: 'Mechanics Sprint' },
                ].map((item, idx) => (
                  <div key={idx} className="p-3 bg-slate-900 border border-slate-800 rounded-xl flex items-center justify-between">
                    <div>
                      <span className="text-[10px] font-mono text-blue-400 font-bold">{item.time}</span>
                      <h4 className="text-xs font-bold text-white">{item.title}</h4>
                      <p className="text-[10px] text-slate-400">{item.sub}</p>
                    </div>
                    <CheckCircle className="w-4 h-4 text-emerald-400" />
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* 8. AFFORDABLE PASS VIEW */}
          {activeTab === 'pass' && (
            <div className="space-y-3 animate-fadeIn">
              <div className="p-4 bg-gradient-to-br from-slate-900 to-indigo-950/60 border border-blue-500/40 rounded-2xl space-y-2">
                <span className="text-[10px] font-bold px-2 py-0.5 bg-amber-500 text-slate-950 rounded uppercase">
                  Universal Education Access
                </span>
                <h3 className="text-base font-bold text-white">Affordable Coaching Pass: ₹99/mo</h3>
                <p className="text-xs text-slate-300">
                  Kota & Hyderabad pedagogy at 1% of the cost. Unlimited 4K classes, 24/7 AI doubts, and NTA mocks.
                </p>
              </div>

              {/* Verified Merchant UPI IDs */}
              <div className="p-3 bg-slate-900 border border-slate-800 rounded-xl space-y-2">
                <span className="text-xs font-bold text-slate-200">Verified Merchant UPI Payment:</span>
                <div className="p-2 bg-slate-950 rounded-lg flex items-center justify-between border border-slate-800">
                  <code className="text-xs text-amber-300 font-mono font-bold">7339956247@ptyes</code>
                  <span className="text-[10px] text-slate-500">Paytm / All UPI</span>
                </div>
                <div className="p-2 bg-slate-950 rounded-lg flex items-center justify-between border border-slate-800">
                  <code className="text-xs text-amber-300 font-mono font-bold">7339956247@ybl</code>
                  <span className="text-[10px] text-slate-500">PhonePe / All UPI</span>
                </div>
              </div>

              {/* 12-Digit UTR Input */}
              <div className="space-y-2">
                <label className="text-xs font-semibold text-slate-300">Enter 12-Digit UTR / Transaction Reference:</label>
                <div className="flex gap-2">
                  <input
                    type="text"
                    placeholder="e.g. 423589123456"
                    value={inputUtr}
                    onChange={(e) => setInputUtr(e.target.value)}
                    className="flex-1 bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-xs text-white font-mono focus:outline-none focus:border-blue-500"
                  />
                  <button
                    onClick={() => {
                      if (inputUtr.trim().length >= 8) {
                        setIsPassActive(true);
                        setUtrSuccess(true);
                        setTimeout(() => setUtrSuccess(false), 4000);
                      } else {
                        alert('Please enter a valid 12-digit UTR reference ID.');
                      }
                    }}
                    className="px-4 py-2 bg-emerald-500 hover:bg-emerald-400 text-slate-950 rounded-xl text-xs font-bold"
                  >
                    Verify UTR
                  </button>
                </div>
                {utrSuccess && (
                  <p className="text-xs text-emerald-400 font-bold">
                    ✓ Verified! Pro Coaching Pass unlocked for UTR #{inputUtr}.
                  </p>
                )}
              </div>
            </div>
          )}
        </div>

        {/* Bottom App Navigation Bar */}
        <div className="h-14 bg-slate-900 border-t border-slate-800 flex items-center justify-around px-1 z-30">
          <button
            onClick={() => setActiveTab('home')}
            className={`flex flex-col items-center gap-0.5 py-1 px-2 rounded-lg transition ${
              activeTab === 'home' ? 'text-blue-400 font-bold' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <Home className="w-4 h-4" />
            <span className="text-[9px]">Home</span>
          </button>

          <button
            onClick={() => {
              setActiveTab('classroom');
              speakText(currentTeacher.speechAudioText);
            }}
            className={`flex flex-col items-center gap-0.5 py-1 px-2 rounded-lg transition ${
              activeTab === 'classroom' ? 'text-blue-400 font-bold' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <BookOpen className="w-4 h-4" />
            <span className="text-[9px]">Class</span>
          </button>

          <button
            onClick={() => setActiveTab('faculty')}
            className={`flex flex-col items-center gap-0.5 py-1 px-2 rounded-lg transition ${
              activeTab === 'faculty' ? 'text-blue-400 font-bold' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <Users className="w-4 h-4" />
            <span className="text-[9px]">Faculty</span>
          </button>

          <button
            onClick={() => setActiveTab('practice')}
            className={`flex flex-col items-center gap-0.5 py-1 px-2 rounded-lg transition ${
              activeTab === 'practice' ? 'text-blue-400 font-bold' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <Dumbbell className="w-4 h-4" />
            <span className="text-[9px]">Practice</span>
          </button>

          <button
            onClick={() => setActiveTab('tests')}
            className={`flex flex-col items-center gap-0.5 py-1 px-2 rounded-lg transition ${
              activeTab === 'tests' ? 'text-blue-400 font-bold' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <Award className="w-4 h-4" />
            <span className="text-[9px]">Tests</span>
          </button>

          <button
            onClick={() => setActiveTab('mistakes')}
            className={`flex flex-col items-center gap-0.5 py-1 px-2 rounded-lg transition ${
              activeTab === 'mistakes' ? 'text-blue-400 font-bold' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <AlertCircle className="w-4 h-4" />
            <span className="text-[9px]">Mistakes</span>
          </button>

          <button
            onClick={() => setActiveTab('pass')}
            className={`flex flex-col items-center gap-0.5 py-1 px-2 rounded-lg transition ${
              activeTab === 'pass' ? 'text-amber-400 font-bold' : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            <Shield className="w-4 h-4" />
            <span className="text-[9px]">₹99 Pass</span>
          </button>
        </div>

        {/* Home Indicator line */}
        <div className="h-4 bg-slate-900 flex justify-center items-center pb-1">
          <div className="w-32 h-1 bg-slate-600 rounded-full" />
        </div>
      </div>

      {/* AdMob Short Video Simulation Modal */}
      {showAdMobModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/85 backdrop-blur-sm">
          <div className="w-full max-w-sm bg-slate-900 border border-amber-500/50 rounded-2xl p-5 shadow-2xl space-y-4">
            <div className="flex justify-between items-center">
              <h3 className="font-bold text-white text-sm flex items-center gap-2">
                <Star className="w-4 h-4 text-amber-400 fill-amber-400" />
                AdMob Short Video Rewards Console
              </h3>
              <button 
                disabled={isWatchingAd}
                onClick={() => setShowAdMobModal(false)}
                className="text-slate-400 hover:text-white"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            {isWatchingAd ? (
              <div className="p-6 bg-black rounded-xl text-center space-y-3">
                <div className="w-12 h-12 border-4 border-amber-500 border-t-transparent rounded-full animate-spin mx-auto" />
                <p className="text-xs text-slate-300 font-semibold">
                  Playing Rewarded Video Ad...
                </p>
                <span className="text-2xl font-black text-amber-400 font-mono">00:0{adCountdown}</span>
              </div>
            ) : (
              <div className="space-y-2.5">
                <p className="text-xs text-slate-300">
                  Watch a 5-second short video ad to claim an instant bonus:
                </p>
                <button
                  onClick={() => handleSimulateWatchAd('4K AI Video Masterclass Pass')}
                  className="w-full p-2.5 bg-slate-950 hover:bg-slate-800 border border-amber-500/30 rounded-xl text-left text-xs text-white font-medium flex items-center justify-between"
                >
                  <span>🎥 4K Video Lecture Unlock</span>
                  <span className="text-amber-400 font-bold">Watch &gt;</span>
                </button>
                <button
                  onClick={() => handleSimulateWatchAd('30 Instant AI Doubt Credits')}
                  className="w-full p-2.5 bg-slate-950 hover:bg-slate-800 border border-amber-500/30 rounded-xl text-left text-xs text-white font-medium flex items-center justify-between"
                >
                  <span>🎙️ 30 AI Doubt Credits</span>
                  <span className="text-amber-400 font-bold">Watch &gt;</span>
                </button>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};
