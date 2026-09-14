package com.example.model

enum class DiagramType {
    INCLINED_PLANE_FORCES,
    PULLEY_ATWOOD,
    CARBOCATION_STABILITY,
    CONCEPT_FLOW_GRAPH
}

data class DiagramLabel(
    val text: String,
    val relativeX: Float, // 0f..1f
    val relativeY: Float, // 0f..1f
    val colorHex: Long = 0xFF38BDF8
)

sealed class WhiteboardElement {
    data class TextNotes(
        val heading: String,
        val bulletPoints: List<String>,
        val tag: String = "NOTE"
    ) : WhiteboardElement()

    data class FormulaBox(
        val formula: String,
        val explanation: String,
        val derivationSteps: List<String> = emptyList()
    ) : WhiteboardElement()

    data class InteractiveDiagram(
        val diagramType: DiagramType,
        val title: String,
        val labels: List<DiagramLabel>,
        val angleDegrees: Float = 30f,
        val showVectors: Boolean = true
    ) : WhiteboardElement()

    data class ComparisonTable(
        val title: String,
        val headers: List<String>,
        val rows: List<List<String>>
    ) : WhiteboardElement()

    data class ConceptMap(
        val centralConcept: String,
        val nodes: List<String>
    ) : WhiteboardElement()

    data class HighlightBox(
        val message: String,
        val colorHex: Long = 0xFFF59E0B
    ) : WhiteboardElement()
}

enum class TeachingMethod(val displayName: String, val description: String) {
    STEP_BY_STEP("Step-by-Step Derivation", "Breaking down the governing equations logically"),
    REAL_WORLD_ANALOGY("Real-World Analogy", "Intuitive connection to daily experiences"),
    VISUAL_DIAGRAM("Visual & Vector Diagram", "Geometric, graphical, and physical vector diagrams"),
    WORKED_NUMERICAL("Worked Step-by-Step Example", "Concrete numerical values and trap prevention"),
    VERIFICATION_CHECK("Socratic Concept Check", "Verifying the mental model with a targeted question")
}

enum class AvatarState {
    IDLE,
    SPEAKING,
    LISTENING,
    EXPLAINING_BOARD,
    THINKING
}

data class LiveClassAttendance(
    val topicId: String,
    val topicTitle: String,
    val teacherName: String,
    val totalSecondsAttended: Int,
    val pollsAnswered: Int,
    val doubtsAsked: Int,
    val notesTaken: List<String>
)

data class LiveClassTimelineCheckpoint(
    val stepIndex: Int,
    val title: String,
    val durationSeconds: Int,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)
