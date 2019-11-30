package com.silambar.fragmentcommunicationwitheventbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_person_a.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PersonA : Fragment() {

    private lateinit var messageAdapter: MessageAdapter
    private val messages = ArrayList<String>()
    private var message: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_person_a, container, false)
    }

    override fun onResume() {
        super.onResume()

        sendBtn.setOnClickListener {
            val msg = msgBox.text.toString()
            if (msg.isNotEmpty() && !msg.contentEquals(message)) {
                EventBus.getDefault().post(MessageEvent.MessageB(msg)) // send message to Person B
                msgBox.setText("")
                message = msg
            }
        }

        messageAdapter = MessageAdapter(messages)
        val lManager = LinearLayoutManager(requireContext()).apply { reverseLayout = true }
        msgListA.apply {
            layoutManager = lManager
            adapter = messageAdapter
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onResultReceived(event: MessageEvent) {
        when (event) {
            is MessageEvent.MessageA -> {
                messages.add(event.msg)
                messageAdapter.notifyItemInserted(messages.size)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}