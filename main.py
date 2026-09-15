from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.label import Label
from kivy.uix.button import Button
from kivy.uix.textinput import TextInput
import requests


class HTTPTester(App):
    def build(self):
        layout = BoxLayout(
            orientation="vertical",
            padding=20,
            spacing=10
        )

        self.url = TextInput(
            hint_text="Enter your own HTTP/HTTPS URL",
            multiline=False
        )

        self.result = Label(
            text="Ready",
            size_hint_y=None,
            height=100
        )

        button = Button(
            text="Send Test Request",
            size_hint_y=None,
            height=60
        )
        button.bind(on_press=self.send_request)

        layout.add_widget(self.url)
        layout.add_widget(button)
        layout.add_widget(self.result)

        return layout

    def send_request(self, instance):
        url = self.url.text.strip()

        if not url.startswith(("http://", "https://")):
            self.result.text = "Please enter a valid HTTP/HTTPS URL"
            return

        try:
            response = requests.get(url, timeout=10)
            self.result.text = (
                f"Status: {response.status_code}\n"
                f"Response: {len(response.content)} bytes"
            )
        except Exception as e:
            self.result.text = f"Error: {e}"


if __name__ == "__main__":
    HTTPTester().run()
