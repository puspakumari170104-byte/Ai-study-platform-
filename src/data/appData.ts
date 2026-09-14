export interface Teacher {
  id: string;
  name: string;
  title: string;
  subject: string;
  expertise: string;
  teachingStyle: string;
  language: string;
  avatarColor: string;
  rating: number;
  doubtsSolved: string;
  speechAudioText: string;
  chalkboardEquation: string;
}

export interface Question {
  id: string;
  subject: string;
  chapter: string;
  text: string;
  options: string[];
  correctIndex: number;
  hint: string;
  explanation: string;
  formula: string;
  difficulty: 'Foundation' | 'Moderate' | 'Challenger';
  mistakeType: 'Conceptual Slip' | 'Calculation Error' | 'Examiner Trap' | 'Memory Gap';
}

export interface MistakeItem {
  id: string;
  chapter: string;
  question: string;
  userAnswer: string;
  correctAnswer: string;
  cause: 'Conceptual Slip' | 'Calculation Error' | 'Examiner Trap' | 'Memory Gap';
  revisionCount: number;
  isResolved: boolean;
}

export const initialTeachers: Teacher[] = [
  {
    id: 'vikram',
    name: 'Dr. Vikram Seth',
    title: 'Senior Master Faculty in Physics',
    subject: 'Physics',
    expertise: 'Classical Mechanics, Electrodynamics & Wave Optics',
    teachingStyle: 'First-Principles Visualizer & Socratic Inquirer',
    language: 'Bilingual (English + Hindi / Hinglish)',
    avatarColor: '#3B82F6',
    rating: 4.96,
    doubtsSolved: '142k+',
    speechAudioText: 'Dhyan se dekho bacchon! Friction is a self-adjusting contact force. When a block is on an inclined plane, normal force N equals mg cos θ, while the downward slide force is mg sin θ. As long as mg sin θ does not exceed limiting friction μs N, the block will NOT move!',
    chalkboardEquation: 'F_net = m·a | f_s ≤ μ_s·N | a = g(sin θ - μ_k·cos θ)'
  },
  {
    id: 'ananya',
    name: 'Dr. Ananya Sharma',
    title: 'Senior Professor of Life Sciences',
    subject: 'Biology',
    expertise: 'Human Physiology, Genetics & Cell Biology',
    teachingStyle: 'NCERT Word-to-Word & Clinical Mnemonics',
    language: 'Bilingual (English + Hindi / Hinglish)',
    avatarColor: '#EC4899',
    rating: 4.98,
    doubtsSolved: '189k+',
    speechAudioText: 'Remember students, Singer and Nicolson established the Fluid Mosaic Model in 1972. The quasi-fluid nature of the lipid bilayer permits lateral movement of proteins! And note this NCERT exam trap: Mitochondria and chloroplasts are NOT part of the endomembrane system.',
    chalkboardEquation: 'Fluid Mosaic Model (1972) | Endomembrane = ER + Golgi + Lysosomes + Vacuoles'
  },
  {
    id: 'priya',
    name: 'Prof. Priya Raman',
    title: 'Chief Faculty of Chemistry',
    subject: 'Chemistry',
    expertise: 'Organic Mechanisms & Chemical Equilibrium',
    teachingStyle: '3D Molecular Orbitals & Arrow Pushing',
    language: 'Bilingual (English + Hindi)',
    avatarColor: '#10B981',
    rating: 4.94,
    doubtsSolved: '115k+',
    speechAudioText: 'Look at the electron displacement: 3° carbocations have 9 alpha-hydrogens, creating maximum hyperconjugative stabilization. Kinetics controls speed, Thermodynamics controls stability at high temperatures!',
    chalkboardEquation: 'Stability: 3° (9 α-H) > 2° (6 α-H) > 1° (3 α-H) | ΔG = ΔH - TΔS'
  },
  {
    id: 'alok',
    name: 'Er. Alok Sinha',
    title: 'Kota Pedagogy Master in Mathematics',
    subject: 'Mathematics',
    expertise: 'Calculus, Vectors & Coordinate Geometry',
    teachingStyle: 'Fast-Paced Speed Shortcuts & Pattern Elimination',
    language: 'Bilingual & English',
    avatarColor: '#8B5CF6',
    rating: 4.95,
    doubtsSolved: '130k+',
    speechAudioText: 'When tackling definite integrals with trigonometric denominators, immediately test Kings Property: integral from a to b of f(x) equals integral of f(a + b - x). The denominators will cancel like magic!',
    chalkboardEquation: '∫[a to b] f(x)dx = ∫[a to b] f(a+b-x)dx | d/dx ∫[u(x) to v(x)] f(t)dt'
  }
];

export const practiceQuestions: Question[] = [
  {
    id: 'q1',
    subject: 'Physics',
    chapter: 'Laws of Motion & Friction',
    text: 'A block of mass 2 kg rests on a horizontal table with coefficient of static friction μs = 0.5. A horizontal pulling force of 4 N is applied. What is the frictional force exerted by the table on the block? (Take g = 10 m/s²)',
    options: ['4 N', '10 N', '5 N', '0 N'],
    correctIndex: 0,
    hint: 'Static friction is self-adjusting! Does 4 N exceed the maximum limiting friction threshold?',
    explanation: 'Limiting friction f_max = μs · N = 0.5 · (2 · 10) = 10 N. Since the applied force (4 N) is LESS than limiting friction (10 N), the block does not move! Static friction exactly matches the applied driving force = 4 N. Many students mistakenly mark 10 N!',
    formula: 'f_s = F_applied (when F_applied ≤ f_max)',
    difficulty: 'Challenger',
    mistakeType: 'Examiner Trap'
  },
  {
    id: 'q2',
    subject: 'Physics',
    chapter: 'Laws of Motion & Pulley Constraints',
    text: 'Two masses m1 = 4 kg and m2 = 6 kg are connected by a light string over a frictionless, massless pulley (Atwood machine). What is the acceleration of the system? (Take g = 10 m/s²)',
    options: ['2.0 m/s²', '1.0 m/s²', '4.0 m/s²', '5.0 m/s²'],
    correctIndex: 0,
    hint: 'The net driving force is the difference in weights. What is the total mass being accelerated?',
    explanation: 'Net driving force = (m2 - m1) · g = (6 - 4) · 10 = 20 N. Total accelerated mass = m1 + m2 = 10 kg. Acceleration a = Net Force / Total Mass = 20 / 10 = 2.0 m/s².',
    formula: 'a = (m2 - m1)g / (m1 + m2)',
    difficulty: 'Moderate',
    mistakeType: 'Calculation Error'
  },
  {
    id: 'q3',
    subject: 'Biology',
    chapter: 'Cell: The Unit of Life',
    text: 'Which of the following cellular organelles is NOT part of the coordinated endomembrane system according to NCERT?',
    options: ['Peroxisomes', 'Endoplasmic Reticulum', 'Golgi Apparatus', 'Lysosomes'],
    correctIndex: 0,
    hint: 'The endomembrane system includes organelles whose functions are coordinated. Which organelle has independent oxidative catalase function?',
    explanation: 'According to NCERT Class 11 Biology, the endomembrane system consists of ER, Golgi complex, Lysosomes, and Vacuoles. Peroxisomes, Mitochondria, and Chloroplasts are NOT part of it because their functions are uncoordinated.',
    formula: 'Endomembrane = ER + Golgi + Lysosomes + Vacuoles',
    difficulty: 'Foundation',
    mistakeType: 'Conceptual Slip'
  },
  {
    id: 'q4',
    subject: 'Chemistry',
    chapter: 'General Organic Chemistry (GOC)',
    text: 'Which of the following carbocations is the most stable due to maximum hyperconjugation structures?',
    options: ['(CH3)3C⁺ (tert-butyl cation)', '(CH3)2CH⁺ (isopropyl cation)', 'CH3-CH2⁺ (ethyl cation)', 'CH3⁺ (methyl cation)'],
    correctIndex: 0,
    hint: 'Count the number of alpha-hydrogens attached to the sp² hybridized carbocation carbon.',
    explanation: 'In tert-butyl carbocation (CH3)3C⁺, there are three methyl groups providing 9 alpha-hydrogens. Hyperconjugative canonical structures = 9 + 1 = 10, giving maximum charge dispersion.',
    formula: 'Stability ∝ Number of α-hydrogens',
    difficulty: 'Foundation',
    mistakeType: 'Memory Gap'
  }
];

export const initialMistakes: MistakeItem[] = [
  {
    id: 'm1',
    chapter: 'Laws of Motion & Friction',
    question: 'A 2 kg body is placed on horizontal plane with μs = 0.5. Applied force = 4 N. Friction force = ?',
    userAnswer: '10 N',
    correctAnswer: '4 N',
    cause: 'Examiner Trap',
    revisionCount: 2,
    isResolved: false
  },
  {
    id: 'm2',
    chapter: 'Laws of Motion & Pulley Constraints',
    question: 'Two connected masses (4 kg and 6 kg) over frictionless pulley. Acceleration = ?',
    userAnswer: '5.0 m/s²',
    correctAnswer: '2.0 m/s²',
    cause: 'Calculation Error',
    revisionCount: 1,
    isResolved: false
  },
  {
    id: 'm3',
    chapter: 'Cell: The Unit of Life',
    question: 'Which organelle is excluded from the endomembrane system in NCERT?',
    userAnswer: 'Lysosomes',
    correctAnswer: 'Peroxisomes',
    cause: 'Conceptual Slip',
    revisionCount: 3,
    isResolved: true
  }
];
