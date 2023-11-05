;;; package --- Summary configuration for alacritty."
;;; Commentary:
;;; Code:
(require 'dash)

(defun start-alacritty ()
  "Start the shell in the specified working directory."
  (interactive)
  (call-process "alacritty"
		nil
		0
		nil
		"--working-directory"
		(-> default-directory expand-file-name shell-quote-argument)))

(provide 'alacritty)
;;; alacritty.el ends here

