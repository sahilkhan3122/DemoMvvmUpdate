package modal

data class ProfileModal(
    val status: Boolean,
    val message: String,
    val data: ProfileData
) {
    inner class ProfileData(
        val id: String?,
        val name: String,
        val email: String
    )
}
