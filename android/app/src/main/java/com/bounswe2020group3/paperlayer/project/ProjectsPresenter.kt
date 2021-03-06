package com.bounswe2020group3.paperlayer.project


import android.content.Intent
import android.net.Uri
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.bounswe2020group3.paperlayer.R
import com.bounswe2020group3.paperlayer.invite.InviteContract
import com.bounswe2020group3.paperlayer.mvp.BasePresenter
import com.bounswe2020group3.paperlayer.profile.ProfileContract
import com.bounswe2020group3.paperlayer.project.data.Tag
import com.bounswe2020group3.paperlayer.request.RequestItem
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


private const val TAG = "ProjectMainPresenter"

class ProjectMainPresenter @Inject constructor(
    private var model: ProjectsContract.Model,
    private val inviteModel: InviteContract.Model,
    private val profileModel: ProfileContract.Model
) : BasePresenter<ProjectsContract.View>(),ProjectsContract.Presenter {

    //Disposable
    private var disposable = CompositeDisposable()

    override fun bind(view: ProjectsContract.View) {
        super.bind(view)
        this.view?.writeLogMessage("i",TAG,"Project Main Presenter Created")
        subscribeAuthToken()
    }

    override fun unbind() {
        super.unbind()
        disposable.clear()
    }

    //set project main fragment as a view of main project presenter
    override fun setView(view: ProjectsContract.View) {
        this.view =view
    }

    override fun showMessage(message: String) {
        this.view?.showToast(message)
    }

    override fun subscribeAuthToken() {
        this.view?.writeLogMessage("i",TAG, "##1##")
        val userProfileSub = model.getAuthToken().subscribe { token ->
            fetchAllProjectsOfUser(token.id)
            fetchAllPublicationsOfOwner(token.id)
            fetchInvitationsOfUser(token.id)
        }
        disposable.add(userProfileSub)
    }

    override fun fetchAllProjectsOfUser(userId: Int) {
        this.view?.writeLogMessage("i",TAG, "Fetching all projects of user $userId ...")
        val getProjectObservable = model.getAllProjectsOfMember(userId).subscribe(
                { projectList ->
                    for (project in projectList){
                        this.view?.addProjectCard(project.name,project.description,project.owner,project.id,project.tags,"Project")
                        this.view?.writeLogMessage("i",TAG,"Project Fetched + " )//+ project.project_type)
                    }
                    this.view?.writeLogMessage("i",TAG,"Fetching finished.")
                    this.view?.submitProjectCardList()
                },
                { error ->
                    this.view?.writeLogMessage("e",TAG,"Error in fetching all projects of user $userId $error")
                }
        )
        disposable.add(getProjectObservable)
    }

    override fun fetchAllPublicationsOfOwner(ownerId: Int) {
        this.view?.writeLogMessage("i",TAG, "Fetching all publications of owner $ownerId ...")
        val getPublicationObservable = model.getAllPublicationsOfOwner(ownerId).subscribe(
                { publicationList ->
                    for (publication in publicationList){
                        var emptyList= ArrayList<Tag>()
                        this.view?.addPublicationCard(publication.title,publication.link,publication.citationNumber.toString(),emptyList,publication.id)
                        this.view?.writeLogMessage("i",TAG,"Publication Fetched + "+publication.title+ " ")//+ project.project_type)
                    }
                    this.view?.writeLogMessage("i",TAG,"Fetching finished.")
                    this.view?.submitPublicationCardList()
                },
                { error ->
                    this.view?.writeLogMessage("e",TAG,"Error in fetching all publications of owner $ownerId $error")
                }
        )
        disposable.add(getPublicationObservable)
    }

    override fun connectScholarPublications(authorId: String) {
        this.view?.writeLogMessage("i",TAG, "Connecting scholar account...")
        val getPublicationObservable = model.addPublicationsOfOwner(authorId).subscribe(
                { response ->
                    this.view?.writeLogMessage("i",TAG,"Connected successfully.")
                    this.view?.showToast("Connected Successfully.")
                    val userProfileSub = model.getAuthToken().subscribe { token ->
                        this.view?.hidePublicationAdd()
                        fetchAllPublicationsOfOwner(token.id)
                    }
                    disposable.add(userProfileSub)
                },
                { error ->
                    this.view?.writeLogMessage("e",TAG,"Error in connecting scholar id of owner $error")
                }
        )
        disposable.add(getPublicationObservable)
    }

    override fun fetchInvitationsOfUser(userId: Int) {
        this.view?.writeLogMessage("i",TAG, "Fetching all invitations of user $userId ...")
        val inviteListSub = inviteModel.getInvitationsOfUser(userId).subscribe(
            { invitationList ->
                for (invite in invitationList){
                    val userObservable = profileModel.getUser(invite.from_user.toInt()).subscribe(
                        { user ->
                            val fullName = "" + user.profile[0].name + user.profile[0].lastName
                            val photoURL = "" + user.profile[0].profile_picture
                            val requestItem = RequestItem(invite.id, user.id, fullName, photoURL, invite.message, invite.to_project)
                            this.view?.addInvitationRequest(requestItem)
                        },
                        { error ->
                            view?.writeLogMessage(
                                "e",
                                TAG,
                                "error while fetching invite requests for user with id ${invite.from_user} $error"
                            )
                        }
                    )
                    disposable.add(userObservable)
                }
                this.view?.writeLogMessage("i",TAG,"Fetching finished.")
                this.view?.submitPublicationCardList()
            },
            { error ->
                this.view?.writeLogMessage("e",TAG,"error while fetching invite requests for user with id $userId $error")
            }
        )
        disposable.add(inviteListSub)
    }


    override fun onViewPublicationButtonClicked(item: ProjectCard, position: Int) {
        //Navigating to publications page
        val url = item.projectBody
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        this.view?.getMyContext()?.startActivity(intent)
    }

    override fun onViewProjectButtonClicked(item: ProjectCard, position: Int) {
        //Navigation to project page, gives projectID as a bundle
        val bundle = bundleOf("projectID" to item.projectId )
        view?.getLayout()?.let { Navigation.findNavController(it).navigate(R.id.navigateToProjectFromProjectMainFragment,bundle) }
    }

    override fun onEditProjectButtonClicked(item: ProjectCard, position: Int) {
        //#FIX# Update this after edit project page added
        //Navigation to project page, gives projectID as a bundle
        val bundle = bundleOf("projectID" to item.projectId )
        view?.getLayout()?.let { Navigation.findNavController(it).navigate(R.id.navigateToProjectFromProjectMainFragment,bundle) }
    }

    override fun onNewProjectButtonClicked() {
        view?.getLayout()?.let { Navigation.findNavController(it).navigate(R.id.navigateToProjectCreateFromProjectMainFragment) }
    }

    override fun acceptInviteRequest(item: RequestItem) {
        val acceptSub = inviteModel.acceptInviteRequest(item.id).subscribe(
            {
                this.view?.showToast("Invite is accepted! $it")
                this.view?.removeInvitationRequest(item)
            },
            {
                this.view?.writeLogMessage("e",TAG,"error while accepting invite requests for invite id ${item.id} $it")
            }
        )
        disposable.add(acceptSub)
    }

    override fun rejectInviteRequest(item: RequestItem) {
        val reject = inviteModel.rejectInviteRequest(item.id).subscribe(
            {
                this.view?.showToast("Invite is rejected!")
                this.view?.removeInvitationRequest(item)
            },
            {
                this.view?.writeLogMessage("e",TAG,"error while rejecting invite requests for invite id ${item.id} $it")
            }
        )
        disposable.add(reject)
    }

}