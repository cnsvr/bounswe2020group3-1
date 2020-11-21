package com.bounswe2020group3.paperlayer.profile.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bounswe2020group3.paperlayer.R
import com.bounswe2020group3.paperlayer.profile.data.Profile
import kotlinx.android.synthetic.main.fragment_profile_edit.*


class ProfileEditFragment : Fragment(), ProfileEditContract.View {

    private lateinit var presenter: ProfileEditContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setPresenter(ProfileEditPresenter(this))
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.subscribeUserProfile()

        editTextFirstName.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
            if(!hasFocus) {
                val firstName = editTextFirstName.text.toString()
                presenter.onFirstNameChange(firstName)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadUserProfile()
    }

    override fun setPresenter(presenter: ProfileEditContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading() {
        progressBarProfileEdit.visibility = View.VISIBLE
        scrollViewProfileEdit.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBarProfileEdit.visibility = View.GONE
        scrollViewProfileEdit.visibility = View.VISIBLE
    }

    override fun showErrorToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showInfoToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun updateProfileUI(profile: Profile) {
        editTextFirstName.setText(profile.name)
        editTextLastName.setText(profile.lastName)
        editTextExpertise.setText(profile.expertise)
        editTextInterests.setText(profile.interests)
        editTextGender.setText(profile.gender)
        editTextAge.setText(profile.age.toString())
        editTextBio.setText(profile.bio)
    }

}