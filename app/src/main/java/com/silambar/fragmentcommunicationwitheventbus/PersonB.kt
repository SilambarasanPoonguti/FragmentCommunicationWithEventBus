package com.silambar.fragmentcommunicationwitheventbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_person_b.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PersonB : Fragment() {

    private lateinit var messageAdapter: MessageAdapter
    private val messages = ArrayList<String>()
    private var message: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_person_b, container, false)
    }

    override fun onResume() {
        super.onResume()

        sendBtn.setOnClickListener {
            val msg = msgBox.text.toString()
            if (msg.isNotEmpty() && !msg.contentEquals(message)) {
                EventBus.getDefault().post(MessageEvent.MessageA(msg))
                message = msg
                msgBox.setText("")
            }
        }

        val lManager = LinearLayoutManager(requireContext()).apply { reverseLayout = true }
        messageAdapter = MessageAdapter(messages)
        msgListB.apply {
            layoutManager = lManager
            adapter = messageAdapter
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onResultReceived(event: MessageEvent) {
        when (event) {
            is MessageEvent.MessageB -> {
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